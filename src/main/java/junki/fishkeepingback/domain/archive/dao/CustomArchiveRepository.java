package junki.fishkeepingback.domain.archive.dao;

import junki.fishkeepingback.domain.archive.dto.ArchiveRes;

import java.util.List;

public interface CustomArchiveRepository {
    List<ArchiveRes> findByUsername(String username);
    boolean existsByUsernameAndArchiveName(String username, String archiveName);
}
