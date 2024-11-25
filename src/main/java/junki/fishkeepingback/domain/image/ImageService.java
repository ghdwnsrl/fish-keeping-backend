package junki.fishkeepingback.domain.image;

import junki.fishkeepingback.domain.comment.exception.PostNotFound;
import junki.fishkeepingback.domain.image.dto.ImageDto;
import junki.fishkeepingback.domain.image.uploader.S3Uploader;
import junki.fishkeepingback.domain.post.Post;
import junki.fishkeepingback.domain.post.dao.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RequiredArgsConstructor
@Service
public class ImageService {
    private final ImageRepository imageRepository;
    private final PostRepository postRepository;
    private final S3Uploader s3Uploader;

    @Transactional
    public List<ImageDto> upload(List<MultipartFile> files) {
        return files.stream()
                .map(s3Uploader::upload)
                .toList();
    }

    @Transactional
    public void save(Long postId, List<ImageDto> images) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new PostNotFound("존재하지 않는 게시물입니다."));
        images.forEach(image -> imageRepository.save(new Image(post, image.originalFilename(), image.filename(), image.url())));
    }
}
