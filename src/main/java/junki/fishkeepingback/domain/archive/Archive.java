package junki.fishkeepingback.domain.archive;

import jakarta.persistence.*;
import junki.fishkeepingback.domain.post.Post;
import junki.fishkeepingback.domain.user.User;
import junki.fishkeepingback.global.config.BaseEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

import static jakarta.persistence.FetchType.*;
import static jakarta.persistence.GenerationType.*;
import static lombok.AccessLevel.*;

@Entity
@Getter
@NoArgsConstructor(access = PROTECTED)
public class Archive extends BaseEntity {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "archive_id")
    private Long id;

    private String name;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "archive")
    private List<Post> posts;

    public Archive(String name, User user) {
        this.name = name;
        this.user = user;
    }

    @Builder
    public Archive(Long id, String name, User user) {
        this.id = id;
        this.name = name;
        this.user = user;
    }
}
