package junki.fishkeepingback.domain.term;

import jakarta.persistence.*;
import junki.fishkeepingback.global.config.BaseEntity;
import lombok.Getter;

@Getter
@Entity
public class Term extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "term_id")
    private Long id;

    @Column(nullable = false, length = 255)
    private String title;

    @Lob
    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    @Column(nullable = false, length = 50)
    private String version;

    @Column(nullable = false)
    private Boolean isActive = true;

}
