package junki.fishkeepingback.domain.archive.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import junki.fishkeepingback.domain.archive.Archive;

import java.time.LocalDateTime;

public record ArchiveRes(
        Long id,
        String name,
        Long totalPosts,
        @JsonFormat(pattern = "yyyy-MM-dd") LocalDateTime lastModified
) {
    public ArchiveRes(Archive archive, Long totalPosts, LocalDateTime lastModified) {
        this(archive.getId(), archive.getName(), totalPosts, lastModified);
    }
}
