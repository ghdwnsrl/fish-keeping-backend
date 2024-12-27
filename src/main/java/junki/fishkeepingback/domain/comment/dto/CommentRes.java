package junki.fishkeepingback.domain.comment.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import junki.fishkeepingback.domain.comment.Comment;

import java.time.LocalDateTime;
import java.util.List;

public record CommentRes(
        Long id,
        String content,
        String username,
        List<CommentDto> replies,
        @JsonFormat(pattern = "yyyy-MM-dd") LocalDateTime createdAt) {
    public CommentRes(Comment comment) {
        this(comment.getId(), comment.getContent(),
                comment.getUser().getUsername(), null, comment.getCreatedAt());
    }

    public CommentRes(Comment comment, List<CommentDto> replies) {
        this(comment.getId(), comment.getContent(),
                comment.getUser().getUsername(), replies, comment.getCreatedAt());
    }

    public CommentRes(Long id, String content, String username, List<CommentDto> replies, LocalDateTime createdAt) {
        this.id = id;
        this.content = content;
        this.username = username;
        this.replies = replies;
        this.createdAt = createdAt;
    }
}
