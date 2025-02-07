package junki.fishkeepingback.domain.archive;

import junki.fishkeepingback.domain.archive.dao.ArchiveRepository;
import junki.fishkeepingback.domain.archive.dto.ArchiveReq;
import junki.fishkeepingback.domain.archive.dto.ArchiveRes;
import junki.fishkeepingback.domain.user.User;
import junki.fishkeepingback.global.error.CommonErrorCode;
import junki.fishkeepingback.global.error.RestApiException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class ArchiveService {

    private final ArchiveRepository archiveRepository;

    public Long create(ArchiveReq archiveReq, User user) {
        isDuplicate(user.getUsername(), archiveReq.name());
        Archive archive = new Archive(archiveReq.name().trim(), user);
        return archiveRepository
                .save(archive).getId();
    }

    private void isDuplicate(String username, String archiveName) {
        if (archiveRepository.existsByUsernameAndArchiveName(username, archiveName)) {
            throw new RestApiException(CommonErrorCode.INVALID_PARAMETER);
        }
    }

    public List<ArchiveRes> findByUsername(String username) {
        return archiveRepository.findByUsername(username);
    }

    public Archive findByArchiveName(String archiveName, User user) {
        return archiveRepository.findByNameAndUserUsername(archiveName, user.getUsername())
                .orElseGet(() -> new Archive(archiveName, user));
    }

    public void delete(String username, String archiveName) {
        Archive archive = archiveRepository.findByNameAndUserUsername(archiveName, username)
                .orElseThrow(() -> new RestApiException(CommonErrorCode.RESOURCE_NOT_FOUND));
        archiveRepository.delete(archive);
    }

    public void update(String archiveName, String updatedName, String username) {
        isDuplicate(username, updatedName);
        archiveRepository.findByNameAndUserUsername(archiveName, username)
                .ifPresent(archive -> archive.update(updatedName));
    }
}