package junki.fishkeepingback.domain.post;

import junki.fishkeepingback.domain.comment.CommentService;
import junki.fishkeepingback.domain.image.ImageService;
import junki.fishkeepingback.domain.postlike.PostLikeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

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
}
