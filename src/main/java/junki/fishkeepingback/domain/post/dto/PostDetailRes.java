package junki.fishkeepingback.domain.post.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import junki.fishkeepingback.domain.post.Post;

import java.time.LocalDateTime;

public record PostDetailRes(
        Long id,
        String title,
        String content,
        String username,
        String profileImageUrl,
        String postImageThumbnailUrl,
        Integer views,
        Boolean liked,
        @JsonFormat(pattern = "yyyy-MM-dd") LocalDateTime createdAt
) {
    public PostDetailRes(Post post, Integer views, Boolean liked) {
        this(post.getId(), post.getTitle(), post.getContent(),
                post.getUser().getUsername(), post.getUser().getProfileImageUrl(), post.getThumbnailUrl(), views, liked,
                post.getCreatedAt());
    }
}
