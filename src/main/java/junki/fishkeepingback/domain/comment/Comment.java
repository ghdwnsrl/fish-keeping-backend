package junki.fishkeepingback.domain.comment;

import jakarta.persistence.*;
import junki.fishkeepingback.domain.post.Post;
import junki.fishkeepingback.domain.user.User;
import junki.fishkeepingback.global.config.BaseEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

import static jakarta.persistence.FetchType.*;
import static jakarta.persistence.GenerationType.*;
import static lombok.AccessLevel.*;

@Entity
@Getter
@NoArgsConstructor(access = PROTECTED)
public class Comment extends BaseEntity {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "comment_id")
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "post_id")
    private Post post;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "parent_id")
    private Comment parent;

    @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> children = new ArrayList<>();

    private String content;

    @Builder
    public Comment(Long id, User user, Post post, String content) {
        this.id = id;
        this.user = user;
        this.post = post;
        this.content = content;
    }

    public void addPost(Post post) {
        this.post = post;
        post.getComments().add(this);
    }

    public void addComment(Comment comment) {
        this.children.add(comment);
        comment.setParent(this);
    }

    private void setParent(Comment parent) {
        this.parent = parent;
    }

    public void update(String content) {
        this.content = content;
    }

}
