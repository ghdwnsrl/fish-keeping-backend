package junki.fishkeepingback.domain.post;

import jakarta.validation.Valid;
import junki.fishkeepingback.domain.image.ImageService;
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

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/posts")
public class PostController {

    private final PostService postService;
    private final ImageService imageService;

    @GetMapping
    public ResponseEntity<Page<PostRes>> getPosts(
            @RequestParam(required = false, defaultValue = "0", value = "page") int pageNo,
            @RequestParam(required = false) String username,
            @RequestParam(required = false) String archiveName
            ) {
        PageRequest pageRequest = PageRequest.of(pageNo, 10);
        postService.getPosts(pageRequest, username, archiveName);
        return ResponseEntity.ok(postService.getPosts(pageRequest, username, archiveName));
    }

    @PostMapping
    public ResponseEntity<Long> createPost(@AuthenticationPrincipal UserDetails userDetails, @Valid @RequestBody PostReq post) {
        String username = userDetails.getUsername();
        Long result = postService.create(post, username);
        imageService.save(result, post.images());
        return ResponseEntity.ok(result);
    }

    @GetMapping("/{postId}")
    public ResponseEntity<PostDetailRes> getPost(@PathVariable Long postId, @AuthenticationPrincipal UserDetails userDetails) {
        PostDetailRes postRes = postService.get(postId, userDetails);
        return ResponseEntity.ok(postRes);
    }

    @DeleteMapping("/{postId}")
    public ResponseEntity<Void> deletePost(@AuthenticationPrincipal UserDetails userDetails, @PathVariable Long postId) {
        postService.delete(postId, userDetails.getUsername());
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{postId}")
    public ResponseEntity<Void> updatePost(@AuthenticationPrincipal UserDetails userDetails, @RequestBody PostReq updatePostDto, @PathVariable Long postId) {
        postService.update(userDetails, updatePostDto, postId);
        imageService.save(postId, updatePostDto.images());
        return ResponseEntity.ok().build();
    }
}