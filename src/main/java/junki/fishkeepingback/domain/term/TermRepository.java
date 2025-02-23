package junki.fishkeepingback.domain.term;

import org.springframework.data.jpa.repository.JpaRepository;

public interface TermRepository extends JpaRepository<Term, Long> {
    Term findByTitle(String title);
}
