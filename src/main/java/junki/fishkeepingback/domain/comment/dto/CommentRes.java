package junki.fishkeepingback.domain.comment.dto;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;
import java.util.List;

public record CommentRes(
        Long id,
        String content,
        String username,
        String profileImageUrl,
        List<CommentDto> replies,
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime createdAt) {
    public CommentRes(Long id, String content, String username, String profileImageUrl, List<CommentDto> replies, LocalDateTime createdAt) {
        this.id = id;
        this.content = content;
        this.username = username;
        this.profileImageUrl = profileImageUrl;
        this.replies = replies;
        this.createdAt = createdAt;
    }
}
