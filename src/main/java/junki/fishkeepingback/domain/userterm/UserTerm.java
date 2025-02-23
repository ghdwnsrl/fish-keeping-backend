package junki.fishkeepingback.domain.userterm;

import jakarta.persistence.*;
import junki.fishkeepingback.domain.term.Term;
import junki.fishkeepingback.domain.user.User;
import junki.fishkeepingback.global.config.BaseEntity;
import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;

import static jakarta.persistence.GenerationType.*;

@Entity
@Getter
public class UserTerm extends BaseEntity {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "term_id", nullable = false)
    private Term term;

    @Column(nullable = false)
    private boolean agreed;

    @CreatedDate
    @Column(updatable = false, name = "agreed_at")
    private LocalDateTime agreedAt;

    public UserTerm(User user, Term term, boolean agreed) {
        this.user = user;
        this.term = term;
        this.agreed = agreed;
    }
}