package junki.fishkeepingback.domain.user;

import jakarta.persistence.*;
import junki.fishkeepingback.domain.comment.Comment;
import junki.fishkeepingback.domain.post.Post;
import junki.fishkeepingback.domain.archive.Archive;
import junki.fishkeepingback.global.config.BaseEntity;
import junki.fishkeepingback.global.config.security.Role;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static jakarta.persistence.CascadeType.*;

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
    private List<Post> posts = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = PERSIST)
    private List<Archive> archives = new ArrayList<>();

    @OneToMany(mappedBy = "user")
    private List<Comment> comments = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    private Role role;

    private String profileImageUrl;
    private String resizedProfileImageUrl;
    private String introText;

    private Boolean isDeleted;

    public User(String username, String password, String profileImageUrl, Role role) {
        this.username = username;
        this.password = password;
        this.profileImageUrl = profileImageUrl;
        this.introText = "";
        this.isDeleted = false;
        this.role = role;
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

    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role.name()));
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
