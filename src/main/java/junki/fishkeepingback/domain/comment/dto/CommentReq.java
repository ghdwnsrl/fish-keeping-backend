package junki.fishkeepingback.domain.comment.dto;

import jakarta.validation.constraints.NotBlank;

public record CommentReq(
        @NotBlank String content,
        Long parentId) {
}
