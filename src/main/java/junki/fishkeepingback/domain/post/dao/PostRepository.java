package junki.fishkeepingback.domain.post.dao;

import junki.fishkeepingback.domain.post.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long>, CustomPostRepository {
    List<Post> findByArchiveNameAndUserUsername(String archiveName, String username);

    @Modifying
    @Query("update Post p set p.views=:viewCount WHERE p.id=:postId")
    void updateViewCount(@Param("postId") Long postId, @Param("viewCount") Long viewCount);
}
