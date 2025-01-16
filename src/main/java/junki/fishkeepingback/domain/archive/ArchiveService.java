package junki.fishkeepingback.domain.archive;

import junki.fishkeepingback.domain.archive.dao.ArchiveRepository;
import junki.fishkeepingback.domain.archive.dto.ArchiveReq;
import junki.fishkeepingback.domain.archive.dto.ArchiveRes;
import junki.fishkeepingback.domain.user.User;
import junki.fishkeepingback.domain.user.UserService;
import junki.fishkeepingback.global.error.CommonErrorCode;
import junki.fishkeepingback.global.error.RestApiException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class ArchiveService {
    private final ArchiveRepository archiveRepository;
    private final UserService userService;

    public Long create(ArchiveReq archiveReq, UserDetails userDetails) {
        User user = Optional.ofNullable(userDetails)
                .map(ud -> userService.findByUsername(ud.getUsername()))
                .orElseThrow(() -> new RestApiException(CommonErrorCode.RESOURCE_NOT_FOUND));
        isDuplicate(archiveReq, user);
        Archive archive = new Archive(archiveReq.name(), user);
        return archiveRepository
                .save(archive).getId();
    }

    private void isDuplicate(ArchiveReq archiveReq, User user) {
        if (archiveRepository.existsByUsernameAndArchiveName(user.getUsername(), archiveReq.name())) {
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
}