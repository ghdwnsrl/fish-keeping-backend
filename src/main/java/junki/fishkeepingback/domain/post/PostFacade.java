package junki.fishkeepingback.domain.post;

import junki.fishkeepingback.domain.comment.CommentService;
import junki.fishkeepingback.domain.image.ImageService;
import junki.fishkeepingback.domain.post.dto.PostDetailRes;
import junki.fishkeepingback.domain.post.dto.PostReq;
import junki.fishkeepingback.domain.post.dto.PostRes;
import junki.fishkeepingback.domain.post.dto.PostSearchParam;
import junki.fishkeepingback.domain.postlike.PostLikeService;
import junki.fishkeepingback.global.response.PageCustom;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Component
public class PostFacade {

    private final PostService postService;
    private final ImageService imageService;
    private final PostLikeService postLikeService;
    private final CommentService commentService;

    @Transactional
    public void deletePost(Long postId, String username) {
        imageService.delete(postId);
        postLikeService.deleteByPostId(postId);
        commentService.deleteByPostId(postId);
        postService.delete(postId, username);
    }

    @Transactional
    public void deletePostsByArchiveName(String archiveName, String username) {
        postService.findByArchiveName(archiveName,username)
                .stream().map(Post::getId)
                .forEach(id -> deletePost(id, username));
    }

    @Transactional
    public void update(UserDetails userDetails, PostReq updatePostDto, Long postId) {
        postService.update(userDetails, updatePostDto, postId);
        imageService.save(postId, updatePostDto.images());
    }

    @Transactional(readOnly = true)
    public List<PostRes> getPopularPost() {
        return postService.getPopularPost();
    }

    @Transactional
    public Long create(PostReq post, String username) {
        Long result = postService.create(post, username);
        imageService.save(result, post.images());
        return result;
    }

    @Transactional(readOnly = true)
    public PostDetailRes getPost(Long postId, UserDetails userDetails) {
        return postService.get(postId, userDetails);
    }

    @Transactional(readOnly = true)
    public PageCustom<PostRes> getPosts(PageRequest pageRequest, String username, String archiveName, PostSearchParam postSearchParam) {
        return postService.getPosts(pageRequest, username, archiveName, postSearchParam);
    }
}
