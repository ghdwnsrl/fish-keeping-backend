package junki.fishkeepingback.domain.comment.dao;

import junki.fishkeepingback.domain.comment.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CommentRepository extends JpaRepository<Comment, Long>, CustomCommentRepository {
    @Modifying
    @Query("DELETE FROM Comment c WHERE c.post.id = :postId and c.parent is not null")
    void deleteChildCommentsByPostId(@Param("postId") Long postId);

    @Modifying
    @Query("DELETE FROM Comment c WHERE c.post.id = :postId and c.parent is null")
    void deleteParentCommentsByPostId(@Param("postId") Long postId);

    @Modifying
    @Query("DELETE FROM Comment c WHERE c.user.id = :userId")
    void deleteChildCommentsByUserId(@Param("userId") Long userId);
}
