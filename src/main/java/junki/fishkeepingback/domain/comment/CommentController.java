package junki.fishkeepingback.domain.comment;

import jakarta.validation.Valid;
import junki.fishkeepingback.domain.comment.dto.CommentReq;
import junki.fishkeepingback.domain.comment.dto.CommentRes;
import junki.fishkeepingback.domain.post.Post;
import junki.fishkeepingback.domain.post.PostService;
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
@RequestMapping("/api")
public class CommentController {

    private final CommentService commentService;
    private final PostService postService;

    @GetMapping("/comments")
    public Page<CommentRes> getComments(
            @RequestParam(required = false, defaultValue = "0", value = "page") int pageNo) {
        PageRequest pageRequest = PageRequest.of(pageNo, 5);
        return commentService.getCommentList(pageRequest);
    }

    @PostMapping("/{postId}/comments")
    public ResponseEntity<Void> create(@PathVariable Long postId, @AuthenticationPrincipal UserDetails userDetails, @Valid @RequestBody CommentReq commentReq) {
        Post post = postService.findById(postId);
        commentService.create(userDetails.getUsername(), post, commentReq);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{commentId}")
    public ResponseEntity<Void> deleteComment(@AuthenticationPrincipal UserDetails userDetails, @PathVariable Long commentId) {
        log.info("delete comment {}", commentId);
        commentService.delete(commentId, userDetails.getUsername());
        return ResponseEntity.ok().build();
    }
}