package junki.fishkeepingback.domain.archive;

import junki.fishkeepingback.domain.archive.dto.ArchiveReq;
import junki.fishkeepingback.domain.archive.dto.ArchiveRes;
import junki.fishkeepingback.global.Result;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/tags")
public class ArchiveController {

    private final ArchiveService archiveService;

    @PostMapping
    public ResponseEntity<Long> create(@RequestBody ArchiveReq archiveReq) {
        Long tagId = archiveService.create(archiveReq);
        return ResponseEntity.ok(tagId);
    }

    @GetMapping
    public ResponseEntity<Result> getAll(@AuthenticationPrincipal UserDetails userDetails) {
        String username = userDetails.getUsername();
        List<ArchiveRes> tags = archiveService.findByUsername(username);
        Result result = Result.builder()
                .status(HttpStatus.OK.value())
                .data(tags)
                .build();
        return ResponseEntity.ok(result);
    }
}