package junki.fishkeepingback.domain.image.uploader;

import junki.fishkeepingback.domain.image.dto.ImageDto;

import java.util.List;

public record ImageUploadRes(
        Long postId,
        List<ImageDto> images
) {
}
