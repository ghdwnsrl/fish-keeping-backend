package junki.fishkeepingback.domain.post.error;

import junki.fishkeepingback.global.error.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum PostError implements ErrorCode {
    POST_NOT_FOUND(HttpStatus.NOT_FOUND,"게시글을 찾을 수 없습니다."),;
    private final HttpStatus httpStatus;
    private final String message;
}
