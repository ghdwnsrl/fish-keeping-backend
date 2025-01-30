package junki.fishkeepingback.domain.post;

import jakarta.persistence.*;
import junki.fishkeepingback.domain.comment.Comment;
import junki.fishkeepingback.domain.archive.Archive;
import junki.fishkeepingback.domain.image.Image;
import junki.fishkeepingback.domain.user.User;
import junki.fishkeepingback.global.config.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

import static jakarta.persistence.FetchType.*;
import static jakarta.persistence.GenerationType.*;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Post extends BaseEntity {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "post_id")
    private Long id;

    private String title;

    @Lob
    @Column(columnDefinition = "TEXT")
    private String content;

    private Integer views;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = LAZY, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "archive_id")
    private Archive archive;

    @OneToMany(mappedBy = "post")
    private List<Comment> comments;

    private String thumbnailUrl;

    public void addUser(User user) {
        if (this.user != null) {
            this.user.getPosts().remove(this);
        }
        this.user = user;
        user.getPosts().add(this);
    }

    public void addArchive(Archive archive) {
        if (this.archive != null) {
            this.archive.getPosts().remove(this);
        }
        this.archive = archive;
        archive.getPosts().add(this);
    }

    public void increaseViews() {
        this.views++;
    }

    public void update(String title, String content, String thumbnailUrl) {
        this.title = title;
        this.content = content;
        this.thumbnailUrl = thumbnailUrl;
        // TODO : Archive 도 설정해줘야함
    }
}