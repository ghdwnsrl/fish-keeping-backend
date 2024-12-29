package junki.fishkeepingback.domain.postlike;

import junki.fishkeepingback.domain.post.Post;
import junki.fishkeepingback.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostLikeRepository extends JpaRepository<PostLike, Long> {
    void deleteByPostIdAndUserId(Long postId, Long userId);
    boolean existsByPostAndUser(Post post, User user);
}
