package junki.fishkeepingback.domain.user.error;

import junki.fishkeepingback.global.error.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum UserError implements ErrorCode {
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 사용자입니다."),
    PASSWORD_MISMATCH(HttpStatus.UNAUTHORIZED, "비밀번호가 일치하지 않습니다. (남은 시도 횟수: %d회)"),
    LOGIN_ATTEMPTS_EXCEEDED(HttpStatus.FORBIDDEN, "로그인 시도 횟수를 초과했습니다. 10분 뒤 다시 시도해주세요.");
    private final HttpStatus httpStatus;
    private final String message;

    public String getFormattedMessage(Object... args) {
        return String.format(this.message, args);
    }
}
