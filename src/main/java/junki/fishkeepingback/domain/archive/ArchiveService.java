package junki.fishkeepingback.domain.archive;

import junki.fishkeepingback.domain.archive.dao.ArchiveRepository;
import junki.fishkeepingback.domain.archive.dto.ArchiveReq;
import junki.fishkeepingback.domain.archive.dto.ArchiveRes;
import junki.fishkeepingback.domain.user.User;
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
        return archiveRepository.findByUsername(username);
    }

    public Archive findByArchiveName(String archiveName, User user) {
        return archiveRepository.findByName(archiveName)
                .orElseGet(() -> new Archive(archiveName, user));
    }
}