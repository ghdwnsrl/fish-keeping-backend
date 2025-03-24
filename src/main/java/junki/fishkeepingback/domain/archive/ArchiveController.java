package junki.fishkeepingback.domain.archive;

import junki.fishkeepingback.domain.archive.dto.ArchiveReq;
import junki.fishkeepingback.domain.archive.dto.ArchiveRes;
import junki.fishkeepingback.domain.post.Post;
import junki.fishkeepingback.domain.post.PostFacade;
import junki.fishkeepingback.domain.post.PostService;
import junki.fishkeepingback.global.Result;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/archives")
public class ArchiveController {

    private final ArchiveFacade archiveFacade;

    @PostMapping
    public ResponseEntity<Long> create(@AuthenticationPrincipal UserDetails userDetails , @RequestBody ArchiveReq archiveReq) {
        Long tagId = archiveFacade.create(archiveReq, userDetails);
        return ResponseEntity.ok(tagId);
    }

    @GetMapping
    public ResponseEntity<Result> getAll(@RequestParam String username) {
        List<ArchiveRes> tags = archiveFacade.findByUsername(username);
        Result result = Result.builder()
                .status(HttpStatus.OK.value())
                .data(tags)
                .build();
        return ResponseEntity.ok(result);
    }

    @DeleteMapping("/{name}")
    public ResponseEntity<Result> delete(@AuthenticationPrincipal UserDetails userDetails , @PathVariable(name = "name") String archiveName) {
        archiveFacade.delete(userDetails, archiveName);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{archiveName}")
    public ResponseEntity<Result> update(@AuthenticationPrincipal UserDetails userDetails , @PathVariable(name = "archiveName") String archiveName, @RequestBody ArchiveReq archiveReq ) {
        String updatedName = archiveReq.name();
        archiveFacade.update(archiveName, updatedName, userDetails);
        return ResponseEntity.ok().build();
    }
}