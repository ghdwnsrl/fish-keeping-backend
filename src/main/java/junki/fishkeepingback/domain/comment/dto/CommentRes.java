package junki.fishkeepingback.domain.comment.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import junki.fishkeepingback.domain.comment.Comment;

import java.time.LocalDateTime;

public record CommentRes(
        Long id,
        String content,
        String username,
        @JsonFormat(pattern = "yyyy-MM-dd") LocalDateTime createdAt) {
    public CommentRes(Comment comment) {
        this(comment.getId(), comment.getContent(),
                comment.getUser().getUsername(), comment.getCreatedAt());
    }
}
