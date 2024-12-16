package junki.fishkeepingback.domain.user.error;

import junki.fishkeepingback.global.error.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum JoinError implements ErrorCode {
    DUPLICATE_USERNAME(HttpStatus.BAD_REQUEST, "아이디가 중복되었습니다."),
    INVALID_PASSWORD(HttpStatus.BAD_REQUEST, "올바르지 않은 비밀번호 형식입니다."),
    CONFIRM_PASSWORD_MISMATCH(HttpStatus.BAD_REQUEST, "비밀번호 재입력이 일치하지 않습니다."),;
    private final HttpStatus httpStatus;
    private final String message;
}