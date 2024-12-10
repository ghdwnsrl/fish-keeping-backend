package junki.fishkeepingback.domain.post.dto;

import jakarta.validation.constraints.NotBlank;

public record UpdatePostDto(
        @NotBlank String title,
        @NotBlank String content,
        String archiveName
) { }
