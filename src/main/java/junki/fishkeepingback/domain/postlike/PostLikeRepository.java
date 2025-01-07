package junki.fishkeepingback.domain.postlike;

import junki.fishkeepingback.domain.post.Post;
import junki.fishkeepingback.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PostLikeRepository extends JpaRepository<PostLike, Long> {
    void deleteByPostIdAndUserId(Long postId, Long userId);
    boolean existsByPostAndUser(Post post, User user);

    @Query(value = "delete from PostLike pl where pl.post.id = :postId")
    @Modifying
    void deleteByPostId(@Param(value = "postId")  Long postId);
}
