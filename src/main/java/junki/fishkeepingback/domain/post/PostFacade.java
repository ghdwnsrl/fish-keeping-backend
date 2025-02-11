package junki.fishkeepingback.domain.post;

import junki.fishkeepingback.domain.archive.Archive;
import junki.fishkeepingback.domain.archive.ArchiveService;
import junki.fishkeepingback.domain.comment.CommentService;
import junki.fishkeepingback.domain.image.Image;
import junki.fishkeepingback.domain.image.ImageService;
import junki.fishkeepingback.domain.image.uploader.S3Uploader;
import junki.fishkeepingback.domain.post.dto.PostDetailRes;
import junki.fishkeepingback.domain.post.dto.PostReq;
import junki.fishkeepingback.domain.post.dto.PostRes;
import junki.fishkeepingback.domain.post.dto.PostSearchParam;
import junki.fishkeepingback.domain.postlike.PostLikeService;
import junki.fishkeepingback.domain.user.User;
import junki.fishkeepingback.domain.user.UserService;
import junki.fishkeepingback.global.ViewCountService;
import junki.fishkeepingback.global.response.PageCustom;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Slf4j
@RequiredArgsConstructor
@Component
public class PostFacade {

    private final PostService postService;
    private final ImageService imageService;
    private final PostLikeService postLikeService;
    private final CommentService commentService;
    private final UserService userService;
    private final ArchiveService archiveService;
    private final S3Uploader s3Uploader;
    private final ViewCountService viewCountService;

    @Transactional
    public void deletePost(Long postId, String username) {
        List<Image> target = imageService.delete(postId);
        target.forEach(image -> s3Uploader.delete(image.getFileName()));
        postLikeService.deleteByPostId(postId);
        commentService.deleteByPostId(postId);
        Post post = postService.findById(postId);
        postService.delete(post, username);
        String[] urlParts = post.getThumbnailUrl().split("/");
        String filename = urlParts[urlParts.length - 1];
        s3Uploader.delete(filename);
    }

    @Transactional
    public void deletePostsByArchiveName(String archiveName, String username) {
        postService.findByArchiveName(archiveName,username)
                .stream().map(Post::getId)
                .forEach(id -> deletePost(id, username));
    }

    @Transactional
    public void update(PostReq updatePostDto, Long postId) {
        Post post = postService.update(updatePostDto, postId);
        if (!post.getThumbnailUrl().equals(updatePostDto.thumbnailUrl())) {
            String[] urlParts = post.getThumbnailUrl().split("/");
            String filename = urlParts[urlParts.length - 1];
            s3Uploader.delete(filename);
        }
        imageService.save(post, updatePostDto.images());
    }

    @Transactional(readOnly = true)
    public List<PostRes> getPopularPost() {
        return postService.getPopularPost();
    }

    @Transactional
    public Long create(PostReq postReq, String username) {
        User user = userService.findByUsername(username);
        Archive archive = archiveService.findByArchiveName(postReq.archiveName(), user);
        Post post = postService.create(postReq, archive, user);
        imageService.save(post, postReq.images());
        return post.getId();
    }

    @Transactional
    public PostDetailRes getPost(Long postId, UserDetails userDetails) {
        User user = Optional.ofNullable(userDetails)
                .map(ud -> userService.findByUsername(ud.getUsername()))
                .orElse(null);
        Post post = postService.get(postId);
        Integer views = viewCountService.incrementViewCount(postId, post.getViews());
        boolean isLiked = postLikeService.getIsLiked(user, post);
        return new PostDetailRes(post, views, isLiked);
    }

    @Transactional(readOnly = true)
    public PageCustom<PostRes> getPosts(PageRequest pageRequest, String username, String archiveName, PostSearchParam postSearchParam) {
        Page<PostRes> posts = postService.getPosts(pageRequest, username, archiveName, postSearchParam);
        List<String> cachedPosts = viewCountService.getKeys(posts);

        List<PostRes> result = IntStream.rangeClosed(0, cachedPosts.size() - 1)
                .mapToObj(idx -> {
                    PostRes postRes = posts.getContent().get(idx);
                    Optional<PostRes> cachedPostRes = Optional.ofNullable(cachedPosts.get(idx))
                            .map(Integer::parseInt)
                            .map(postRes::setCachedViews);
                    return cachedPostRes.orElse(postRes);
                })
                .toList();

        return new PageCustom<>(result, posts.getPageable(), posts.getTotalElements());
    }
}
