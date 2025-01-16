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
    private final PostRepository postRepository;
    private final S3Uploader s3Uploader;


    @Transactional
    public void save(Long postId, List<ImageDto> images) {
        if (images.isEmpty())
            return;
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RestApiException(PostError.POST_NOT_FOUND));
        images.forEach(image -> imageRepository.save(new Image(post, image.url(), image.url().substring(image.url().lastIndexOf("/") + 1))));
    }

    @Transactional
    public void delete(Long postId) {
        List<Image> target = imageRepository.findByPostId(postId);
        // s3에 삭제 해야함. 병렬처리 할까 말까
        target.forEach(image -> {
            log.info(image.getUrl());
        });
        imageRepository.deleteByPostId(postId);
    }

    @Transactional
    public void deleteByStoreName(String storeName) {
        imageRepository.deleteByFileName(storeName);
        s3Uploader.delete(storeName);
    }
}
