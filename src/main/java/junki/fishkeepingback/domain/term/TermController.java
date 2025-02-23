package junki.fishkeepingback.domain.term;

import junki.fishkeepingback.global.Result;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class TermController {

    private final TermService termService;

    @GetMapping("/api/terms/{title}")
    private ResponseEntity<Result>  getTerms(@PathVariable String title) {
        Result result = Result.builder()
                .status(200)
                .data(termService.getTerm(title)).build();
        return ResponseEntity.ok(result);
    }

}
