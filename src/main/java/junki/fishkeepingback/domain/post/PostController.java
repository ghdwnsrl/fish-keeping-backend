package junki.fishkeepingback.domain.post;

import jakarta.validation.Valid;
import junki.fishkeepingback.domain.post.dto.PostDetailRes;
import junki.fishkeepingback.domain.post.dto.PostReq;
import junki.fishkeepingback.domain.post.dto.PostRes;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/posts")
public class PostController {

    private final PostService postService;

    @GetMapping
    public Page<PostRes> getPosts(
            @RequestParam(required = false, defaultValue = "0", value = "page") int pageNo,
            @RequestParam(required = false) String username,
            @RequestParam(required = false) String archiveName
            ) {
        PageRequest pageRequest = PageRequest.of(pageNo, 10);
        return postService.getPosts(pageRequest, username, archiveName);
    }

    @PostMapping
    public ResponseEntity<Long> createPost(@AuthenticationPrincipal UserDetails userDetails, @Valid @RequestBody PostReq post) {
        String username = userDetails.getUsername();
        Long result = postService.create(post, username);
        return ResponseEntity
                .ok(result);
    }

    @GetMapping("/{postId}")
    public ResponseEntity<PostDetailRes> getPost(@PathVariable Long postId) {
        PostDetailRes postRes = postService.get(postId);
        log.info(postRes.toString());
        return ResponseEntity
                .ok(postRes);
    }
}