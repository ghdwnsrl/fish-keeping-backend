package junki.fishkeepingback.domain.archive;

import junki.fishkeepingback.domain.post.PostFacade;
import junki.fishkeepingback.global.error.CommonErrorCode;
import junki.fishkeepingback.global.error.RestApiException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
@Component
public class ArchiveFacade {
    private final ArchiveService archiveService;
    private final PostFacade  postFacade;

    @Transactional
    public void delete(String username, String archiveName) {
        if (archiveName.equals("선택 안함")) {
            throw new RestApiException(CommonErrorCode.INVALID_PARAMETER);
        }
        postFacade.deletePostsByArchiveName(archiveName, username);
        archiveService.delete(username,archiveName);
    }
}
