package junki.fishkeepingback.domain.archive;

import junki.fishkeepingback.domain.archive.dao.ArchiveRepository;
import junki.fishkeepingback.domain.archive.dto.ArchiveReq;
import junki.fishkeepingback.domain.archive.dto.ArchiveRes;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class ArchiveService {
    private final ArchiveRepository archiveRepository;

    public Long create(ArchiveReq archiveReq) {
        Archive archive = archiveReq.toModel();
        return archiveRepository
                .save(archive).getId();
    }

    public List<ArchiveRes> findByUsername(String username) {
        return archiveRepository.findByUserUsername(username)
                .stream().map(ArchiveRes::new)
                .toList();
    }

    public Archive findByArchiveName(String archiveName) {
        return archiveRepository.findByName(archiveName)
                .orElseGet(Archive::new);
    }
}
