package junki.fishkeepingback.domain.archive.dto;

import junki.fishkeepingback.domain.archive.Archive;

public record ArchiveRes(
        Long id,
        String name
) {
    public ArchiveRes(Archive archive) {
        this(archive.getId(), archive.getName());
    }
}
