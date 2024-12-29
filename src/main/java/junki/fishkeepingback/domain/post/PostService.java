package junki.fishkeepingback.domain.post;

import junki.fishkeepingback.domain.archive.Archive;
import junki.fishkeepingback.domain.archive.ArchiveService;
import junki.fishkeepingback.domain.image.ImageService;
import junki.fishkeepingback.domain.image.uploader.S3Uploader;
import junki.fishkeepingback.domain.post.dao.PostRepository;
import junki.fishkeepingback.domain.post.dto.PostDetailRes;
import junki.fishkeepingback.domain.post.dto.PostReq;
import junki.fishkeepingback.domain.post.dto.PostRes;
import junki.fishkeepingback.domain.post.error.PostError;
import junki.fishkeepingback.domain.postlike.PostLikeRepository;
import junki.fishkeepingback.domain.user.User;
import junki.fishkeepingback.domain.user.UserService;
import junki.fishkeepingback.global.error.RestApiException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class PostService {
    private final PostRepository postRepository;
    private final UserService userService;
    private final ArchiveService archiveService;
    private final ImageService imageService;
    private final S3Uploader s3Uploader;
    private final PostLikeRepository postLikeRepository;

    @Transactional
    public Long create(PostReq postReq, String username) {
        User user = userService.findByUsername(username);
        Archive archive = archiveService.findByArchiveName(postReq.archiveName(), user);
        Post post = Post.builder()
                .title(postReq.title())
                .content(postReq.content())
                .thumbnailUrl(postReq.thumbnailUrl())
                .archive(archive)
                .user(user)
                .views(0)
                .build();
        post.addUser(user);
        return postRepository.save(post).getId();
    }

    @Transactional(readOnly = true)
    public Post findById(Long postId) {
        return postRepository.findById(postId)
                .orElseThrow(() -> new RestApiException(PostError.POST_NOT_FOUND));
    }

    @Transactional(readOnly = true)
    public Page<PostRes> getPosts(PageRequest pageRequest, String username, String archiveName) {
        return postRepository.findByUsername(username, archiveName, pageRequest);
    }

    @Transactional
    public PostDetailRes get(Long postId, UserDetails userDetails) {
        Post post = findById(postId);
        post.increaseViews();
        boolean isLiked = getIsLiked(userDetails, post);
        return new PostDetailRes(post, isLiked);
    }

    public boolean getIsLiked(UserDetails userDetails, Post post) {
        return Optional.ofNullable(userDetails)
                .map(ud -> this.someOtherLogic(ud, post))
                .orElse(false);
    }


    private boolean someOtherLogic(UserDetails userDetails, Post post) {
        User user = userService.findByUsername(userDetails.getUsername());
        return postLikeRepository.existsByPostAndUser(post, user);
    }

    public void delete(Long postId, String username) {
        imageService.delete(postId);
        postRepository.findById(postId)
                .ifPresent(post -> {
                    if (isOwner(username, post))
                        postRepository.deleteById(postId);
                });
    }

    @Transactional
    public void update(UserDetails userDetails, PostReq updatePostDto, Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RestApiException(PostError.POST_NOT_FOUND));
        log.info(post.getThumbnailUrl());
        log.info(updatePostDto.thumbnailUrl());
        if (!post.getThumbnailUrl().equals(updatePostDto.thumbnailUrl())) {
            log.info("기존 썸네일이 아닌 다른 썸네일이 들어옴...");
            String[] urlParts = post.getThumbnailUrl().split("/");
            String filename = urlParts[urlParts.length - 1];
            s3Uploader.delete(filename);
        }
        post.update(
                updatePostDto.title(),
                updatePostDto.content(),
                updatePostDto.thumbnailUrl()
        );
    }

    private boolean isOwner(String username, Post post) {
        return post.getUser().getUsername().equals(username);
    }
}
