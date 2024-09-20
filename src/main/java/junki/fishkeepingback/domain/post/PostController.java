package junki.fishkeepingback.domain.post;

import jakarta.validation.Valid;
import junki.fishkeepingback.domain.post.dto.PostReq;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    @PostMapping("/api/posts")
    public ResponseEntity<Long> createPost(@AuthenticationPrincipal UserDetails userDetails, @Valid @RequestBody PostReq post) {
        String username = userDetails.getUsername();
        Long result = postService.create(post, username);
        return ResponseEntity
                .ok(result);
    }
}
