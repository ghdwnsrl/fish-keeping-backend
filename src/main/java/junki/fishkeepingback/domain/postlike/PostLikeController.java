package junki.fishkeepingback.domain.postlike;

import junki.fishkeepingback.domain.post.Post;
import junki.fishkeepingback.domain.post.PostService;
import junki.fishkeepingback.domain.user.User;
import junki.fishkeepingback.domain.user.UserService;
import junki.fishkeepingback.global.error.CommonErrorCode;
import junki.fishkeepingback.global.error.RestApiException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Slf4j
@RequestMapping("/api")
@RestController
@RequiredArgsConstructor
public class PostLikeController {

    private final PostLikeService postLikeService;
    private final PostService postService;
    private final UserService userService;

    @PostMapping("/{postId}/like")
    public ResponseEntity<?> likePost(@PathVariable Long postId, @AuthenticationPrincipal UserDetails userDetails) {
        Post post = postService.findById(postId);
        User user = userService.findByUsername(userDetails.getUsername());
        postLikeService.create(post, user);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{postId}/like")
    public ResponseEntity<?> unlikePost(@PathVariable Long postId, @AuthenticationPrincipal UserDetails userDetails) {
        User user = userService.findByUserDetails(userDetails);
        postLikeService.delete(postId, user.getId());
        return ResponseEntity.ok().build();
    }
}
