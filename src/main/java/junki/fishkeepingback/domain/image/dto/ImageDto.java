package junki.fishkeepingback.domain.image.dto;

public record ImageDto(
        String originalFilename,
        String filename,
        String url
) { }
