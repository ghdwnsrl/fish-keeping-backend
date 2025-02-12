package junki.fishkeepingback.domain.image.dto;

import junki.fishkeepingback.domain.image.ImageType;

public record ImageDto(
        String url,
        ImageType imageType
) { }