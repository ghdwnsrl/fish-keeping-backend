package junki.fishkeepingback.domain.comment;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.LocalDateTime;

@Entity
@Table(name = "vw_comment")
public class CommentPathView {

    @Id
    @Column(name = "comment_id")
    private Long commentId;

    private String content;
    private LocalDateTime createdAt;
    private String username;
    private Long parentId;
    private Long postId;
    private Long groupId;

}
