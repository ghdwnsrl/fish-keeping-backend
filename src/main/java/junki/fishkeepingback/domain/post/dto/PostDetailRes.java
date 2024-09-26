package junki.fishkeepingback.domain.post.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import junki.fishkeepingback.domain.comment.dto.CommentRes;
import junki.fishkeepingback.domain.post.Post;

import java.time.LocalDateTime;
import java.util.List;

public record PostDetailRes(
        Long id,
        String title,
        String content,
        String username,
        Integer views,
        @JsonFormat(pattern = "yyyy-MM-dd") LocalDateTime createdAt,
        List<CommentRes> comments
) {
    public PostDetailRes(Post post, List<CommentRes> comments) {
        this(post.getId(), post.getTitle(), post.getContent(),
                post.getUser().getUsername(), post.getViews(),
                post.getCreatedAt(), List.copyOf(comments));
    }
}
