package junki.fishkeepingback.domain.post;

import jakarta.validation.Valid;
import junki.fishkeepingback.domain.post.dto.PostDetailRes;
import junki.fishkeepingback.domain.post.dto.PostReq;
import junki.fishkeepingback.domain.post.dto.PostRes;
import junki.fishkeepingback.domain.post.dto.PostSearchParam;
import junki.fishkeepingback.global.response.PageCustom;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

    private final PostFacade postFacade;

    @GetMapping
    public ResponseEntity<PageCustom<PostRes>> getPosts(
            @RequestParam(required = false, defaultValue = "1", value = "page") int pageNo,
            @RequestParam(required = false) String username,
            @RequestParam(required = false) String archiveName,
            @PostSearchRequest PostSearchParam postSearchParam
            ) {
        PageRequest pageRequest = PageRequest.of(Math.max(0, pageNo - 1), 10);
        PageCustom<PostRes> result = postFacade.getPosts(pageRequest, username, archiveName, postSearchParam);
        return ResponseEntity.ok(result);
    }

    @PostMapping
    public ResponseEntity<Long> createPost(@AuthenticationPrincipal UserDetails userDetails, @Valid @RequestBody PostReq post) {
        String username = userDetails.getUsername();
        Long result = postFacade.create(post, username);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/{postId}")
    public ResponseEntity<PostDetailRes> getPost(@PathVariable Long postId, @AuthenticationPrincipal UserDetails userDetails) {
        PostDetailRes postRes = postFacade.getPost(postId, userDetails);
        return ResponseEntity.ok(postRes);
    }

    @GetMapping("/popular")
    public ResponseEntity<List<PostRes>> getPopularPost() {
        List<PostRes> postRes = postFacade.getPopularPost();
        return ResponseEntity.ok(postRes);
    }

    @DeleteMapping("/{postId}")
    public ResponseEntity<Void> deletePost(@AuthenticationPrincipal UserDetails userDetails, @PathVariable Long postId) {
        postFacade.deletePost(postId, userDetails);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{postId}")
    public ResponseEntity<Void> updatePost(@RequestBody PostReq updatePostDto, @PathVariable Long postId) {
        postFacade.update(updatePostDto, postId);
        return ResponseEntity.ok().build();
    }
}