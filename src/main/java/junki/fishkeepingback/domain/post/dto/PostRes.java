package junki.fishkeepingback.domain.post.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import junki.fishkeepingback.domain.post.Post;

import java.time.LocalDateTime;

public record PostRes(
        Long id, String title, String username, Integer views,
        @JsonFormat(pattern = "yyyy-MM-dd") LocalDateTime createdAt) {
    public PostRes(Post post) {
        this(post.getId(), post.getTitle(),
                post.getUser().getUsername(), post.getViews(), post.getCreatedAt());
    }
}
