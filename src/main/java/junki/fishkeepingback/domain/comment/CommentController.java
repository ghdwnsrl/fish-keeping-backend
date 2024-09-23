package junki.fishkeepingback.domain.comment;

import jakarta.validation.Valid;
import junki.fishkeepingback.domain.comment.dto.CommentReq;
import junki.fishkeepingback.domain.comment.dto.CommentRes;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/comments")
public class CommentController {

    private final CommentService commentService;

    @GetMapping
    public Page<CommentRes> getComments(
            @RequestParam(required = false, defaultValue = "0", value = "page") int pageNo) {
        PageRequest pageRequest = PageRequest.of(pageNo, 5);
        return commentService.getCommentList(pageRequest);
    }

    @PostMapping
    public ResponseEntity<Void> create(@AuthenticationPrincipal UserDetails userDetails, @Valid @RequestBody CommentReq commentReq) {
        commentService.create(userDetails.getUsername(), commentReq);

        return ResponseEntity.ok().build();
    }
}