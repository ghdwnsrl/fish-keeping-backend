package junki.fishkeepingback.domain.image;

import junki.fishkeepingback.domain.image.dto.ImageDto;
import junki.fishkeepingback.domain.image.dto.ImageRes;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
public class ImageController {

    private final ImageService imageService;

    @PostMapping("/api/images")
    public ResponseEntity<ImageRes> uploadImage(@RequestParam("files") List<MultipartFile> files) {
        log.info("uploading start");
        List<ImageDto> upload = imageService.upload(files);
        ImageRes imageRes = new ImageRes(upload);
        return ResponseEntity
                .ok()
                .body(imageRes);
    }
}
