package junki.fishkeepingback.domain.post.dao;

import junki.fishkeepingback.domain.post.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long>, CustomPostRepository {
    List<Post> findByArchiveNameAndUserUsername(String archiveName, String username);
}
