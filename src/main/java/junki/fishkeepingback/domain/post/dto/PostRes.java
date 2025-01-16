package junki.fishkeepingback.domain.post.dto;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;

public record PostRes(
        Long id,
        String title,
        String username,
        Integer commentCount,
        Integer views,
        String thumbnailUrl,
        Long likeCount,
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime createdAt) { }
