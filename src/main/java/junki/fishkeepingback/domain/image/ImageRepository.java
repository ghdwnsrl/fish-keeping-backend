package junki.fishkeepingback.domain.image;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ImageRepository extends JpaRepository<Image, Long> {
    List<Image> findByPostId(Long postId);

    @Modifying
    @Query("delete from Image i where i.post.id = :id")
    void deleteByPostId(@Param(value = "id") Long postId);

    void deleteByFileName(String storeName);
}
