package junki.fishkeepingback.domain.archive;

import junki.fishkeepingback.domain.archive.dto.ArchiveReq;
import junki.fishkeepingback.domain.archive.dto.ArchiveRes;
import junki.fishkeepingback.domain.post.PostFacade;
import junki.fishkeepingback.domain.user.User;
import junki.fishkeepingback.domain.user.UserService;
import junki.fishkeepingback.global.error.CommonErrorCode;
import junki.fishkeepingback.global.error.RestApiException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Component
public class ArchiveFacade {
    private final ArchiveService archiveService;
    private final UserService userService;
    private final PostFacade  postFacade;

    @Transactional
    public void delete(String username, String archiveName) {
        if (archiveName.equals("선택 안함")) {
            throw new RestApiException(CommonErrorCode.INVALID_PARAMETER);
        }
        postFacade.deletePostsByArchiveName(archiveName, username);
        archiveService.delete(username,archiveName);
    }

    @Transactional
    public Long create(ArchiveReq archiveReq, UserDetails userDetails) {
        User user = Optional.ofNullable(userDetails)
                .map(ud -> userService.findByUsername(ud.getUsername()))
                .orElseThrow(() -> new RestApiException(CommonErrorCode.RESOURCE_NOT_FOUND));
        return archiveService.create(archiveReq, user);
    }

    @Transactional(readOnly = true)
    public List<ArchiveRes> findByUsername(String username) {
        return archiveService.findByUsername(username);
    }

    @Transactional
    public void update(String archiveName, String updatedName, UserDetails userDetails) {
        Optional.ofNullable(userDetails)
                .map(UserDetails::getUsername)
                .ifPresent(username -> archiveService.update(archiveName,updatedName,username));
    }
}
