package junki.fishkeepingback.domain.image;

import junki.fishkeepingback.domain.image.dao.ImageRepository;
import junki.fishkeepingback.domain.image.dto.ImageDto;
import junki.fishkeepingback.domain.image.uploader.S3Uploader;
import junki.fishkeepingback.domain.post.Post;
import junki.fishkeepingback.domain.post.dao.PostRepository;
import junki.fishkeepingback.domain.post.error.PostError;
import junki.fishkeepingback.global.error.RestApiException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class ImageService {

    private final ImageRepository imageRepository;
    private final S3Uploader s3Uploader;

    @Transactional
    public void save(Post post, List<ImageDto> images) {
        if (images.isEmpty()) return;
        images.forEach(image -> {
            String url = image.url();
            String fileName = url.substring(image.url().lastIndexOf("/") + 1);
            imageRepository.save(new Image(post, url, fileName));
        });
    }

    @Transactional
    public List<Image> delete(Long postId) {
        List<Image> target = imageRepository.findByPostId(postId);
        imageRepository.deleteByPostId(postId);
        return target;
    }

    @Transactional
    public void deleteByStoreName(String storeName) {
        imageRepository.deleteByFileName(storeName);
        s3Uploader.delete(storeName);
    }
}
