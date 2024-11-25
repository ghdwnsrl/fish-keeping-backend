package junki.fishkeepingback.domain.post.dto;

import jakarta.validation.constraints.NotBlank;
import junki.fishkeepingback.domain.image.dto.ImageDto;

import java.util.List;

public record PostReq(
        @NotBlank String title,
        @NotBlank String content,
        String archiveName,
        List<ImageDto> images
) { }