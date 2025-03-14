package junki.fishkeepingback.domain.comment;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import org.hibernate.annotations.Immutable;

import java.time.LocalDateTime;

@Immutable
@Entity
@Table(name = "vw_comment")
public class CommentPathView {

    @Id
    @Column(name = "comment_id")
    private Long commentId;

    private String content;
    private LocalDateTime createdAt;
    private String username;
    private String profileImageUrl;
    private Long parentId;
    private Long postId;
    private Long groupId;

}