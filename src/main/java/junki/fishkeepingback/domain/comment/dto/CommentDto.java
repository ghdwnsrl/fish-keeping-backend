package junki.fishkeepingback.domain.comment.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import junki.fishkeepingback.domain.comment.Comment;

import java.time.LocalDateTime;

public record CommentDto(
            Long id,
            String content,
            String username,
            String profileImageUrl,
            @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime createdAt) {
    public CommentDto(Comment comment) {
        this(comment.getId(), comment.getContent(),
                comment.getUser().getUsername(),comment.getUser().getProfileImageUrl(), comment.getCreatedAt());
    }
}
