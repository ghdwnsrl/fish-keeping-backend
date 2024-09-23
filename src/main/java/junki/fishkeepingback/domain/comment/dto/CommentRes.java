package junki.fishkeepingback.domain.comment.dto;

import junki.fishkeepingback.domain.comment.Comment;

import java.time.LocalDateTime;

public record CommentRes(Long id, String content, String username, LocalDateTime createdAt) {
    public CommentRes(Comment comment) {
        this(comment.getId(), comment.getContent(),
                comment.getUser().getUsername(), comment.getCreatedAt());
    }
}
