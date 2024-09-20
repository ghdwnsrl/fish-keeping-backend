package junki.fishkeepingback.domain.post.dto;

import jakarta.validation.constraints.NotBlank;
import junki.fishkeepingback.domain.post.Post;

public record PostReq(@NotBlank String title,
                      @NotBlank String content)
{
    public Post toEntity() {
        return Post.builder()
                .title(title)
                .content(content)
                .build();
    }
}
