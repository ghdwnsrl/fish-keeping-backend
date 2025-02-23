package junki.fishkeepingback.domain.term;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class TermService {
    final TermRepository termRepository;

    public List<Term> getTerms() {
        return termRepository.findAll();
    }

    public String getTerm(String title) {
        Term byTitle = termRepository.findByTitle(title);
        return byTitle.getContent();
    }
}
