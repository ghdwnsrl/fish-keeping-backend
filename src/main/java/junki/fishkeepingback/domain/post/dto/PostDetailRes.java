package junki.fishkeepingback.domain.post.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import junki.fishkeepingback.domain.post.Post;

import java.time.LocalDateTime;

public record PostDetailRes(
        Long id,
        String title,
        String content,
        String username,
        Integer views,
        @JsonFormat(pattern = "yyyy-MM-dd") LocalDateTime createdAt
) {
    public PostDetailRes(Post post) {
        this(post.getId(), post.getTitle(), post.getContent(),
                post.getUser().getUsername(), post.getViews(), post.getCreatedAt());
    }
}
