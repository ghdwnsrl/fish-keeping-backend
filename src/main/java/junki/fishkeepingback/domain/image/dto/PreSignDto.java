package junki.fishkeepingback.domain.image.dto;

import junki.fishkeepingback.domain.post.dto.FileDto;

import java.util.List;

public record PreSignDto(
        List<FileDto> files
) { }
