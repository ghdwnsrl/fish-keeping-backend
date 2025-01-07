package junki.fishkeepingback.domain.post.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import junki.fishkeepingback.domain.comment.dto.CommentRes;
import junki.fishkeepingback.domain.post.Post;
import lombok.ToString;
import org.springframework.data.domain.Page;

import java.time.LocalDateTime;
import java.util.List;

public record PostDetailRes(
        Long id,
        String title,
        String content,
        String username,
        String thumbnailUrl,
        Integer views,
        Boolean liked,
        @JsonFormat(pattern = "yyyy-MM-dd") LocalDateTime createdAt
) {
    public PostDetailRes(Post post, Boolean liked) {
        this(post.getId(), post.getTitle(), post.getContent(),
                post.getUser().getUsername(), post.getThumbnailUrl(), post.getViews(), liked,
                post.getCreatedAt());
    }
}
