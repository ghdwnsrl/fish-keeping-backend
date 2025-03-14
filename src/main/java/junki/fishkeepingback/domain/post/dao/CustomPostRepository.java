package junki.fishkeepingback.domain.post.dao;

import junki.fishkeepingback.domain.post.dto.PostRes;
import junki.fishkeepingback.domain.post.dto.PostSearchParam;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;


public interface CustomPostRepository {
    Page<PostRes> findByUsername(String username, String archiveName, Pageable pageable, PostSearchParam postSearchParam);
    List<PostRes> findByTop3();
}