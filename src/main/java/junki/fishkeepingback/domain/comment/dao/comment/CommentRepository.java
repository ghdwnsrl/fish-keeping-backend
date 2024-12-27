package junki.fishkeepingback.domain.comment.dao.comment;

import junki.fishkeepingback.domain.comment.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long>, CustomCommentRepository {
    List<Comment> findByPostId(Long postId);
}
