package junki.fishkeepingback.domain.image;

import junki.fishkeepingback.domain.image.dto.ImageDto;
import junki.fishkeepingback.domain.image.dto.PreSignDto;
import junki.fishkeepingback.domain.image.uploader.S3Uploader;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
public class ImageController {

    private final ImageService imageService;
    private final S3Uploader s3Uploader;

    @PostMapping("/api/presigned-url")
    public ResponseEntity<List<String>> getPresignedUrl(@RequestBody PreSignDto preSignDto) {
        log.info("getting presigned url {}", preSignDto);
        List<String> preSignedUrls = preSignDto.filenames().stream()
                .map(f -> s3Uploader.getPreSignedUrl(f.fileName(), f.contentType()))
                .toList();
        return ResponseEntity.ok().body(preSignedUrls);
    }

    @DeleteMapping("/api/images/{storeName}")
    public ResponseEntity<Void> deleteImages(@PathVariable String storeName) {
        log.info("deleting images {}", storeName);
        imageService.deleteByStoreName(storeName);
        //s3에서도 지워야함
        return ResponseEntity.ok().build();
    }
}
