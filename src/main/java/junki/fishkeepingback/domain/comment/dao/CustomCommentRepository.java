package junki.fishkeepingback.domain.comment.dao;

import junki.fishkeepingback.domain.comment.dto.CommentRes;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CustomCommentRepository {
    Page<CommentRes> findCommentsByPostId(Long postId, Pageable pageable);
}