package junki.fishkeepingback.domain.user;

import jakarta.persistence.*;
import junki.fishkeepingback.domain.comment.Comment;
import junki.fishkeepingback.domain.post.Post;
import junki.fishkeepingback.domain.archive.Archive;
import junki.fishkeepingback.global.config.BaseEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name = "users")
@NoArgsConstructor
@Getter
public class User extends BaseEntity {
    @Id
    @Column(name = "user_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;
    private String password;

    @OneToMany(mappedBy = "user")
    private List<Post> posts;

    @OneToMany(mappedBy = "user")
    private List<Archive> archives;

    @OneToMany(mappedBy = "user")
    private List<Comment> comments;

    private String profileImageUrl;
    private String resizedProfileImageUrl;
    private String introText;

    private Boolean isDeleted;

    public User(String username, String password, String profileImageUrl) {
        this.username = username;
        this.password = password;
        this.profileImageUrl = profileImageUrl;
        this.introText = "";
        this.isDeleted = false;
    }

    public void deleteSoft() {
        this.isDeleted = true;
    }

    public void updateProfileImage(String profileImageUrl, String resizedProfileImageUrl ) {
        this.profileImageUrl = profileImageUrl;
        this.resizedProfileImageUrl = resizedProfileImageUrl;
    }

    public void updateIntroText(String introText) {
        this.introText = introText;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
