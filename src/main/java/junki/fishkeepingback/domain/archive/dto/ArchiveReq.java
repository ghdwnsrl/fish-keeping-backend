package junki.fishkeepingback.domain.archive.dto;

import jakarta.validation.constraints.NotBlank;
import junki.fishkeepingback.domain.archive.Archive;

public record ArchiveReq(
        @NotBlank String name
) {
    public Archive toModel() {
        return Archive.builder()
                .name(name)
                .build();
    }
}
