package junki.fishkeepingback.global.error;

import junki.fishkeepingback.domain.user.error.UserError;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class RestApiException extends RuntimeException {
    private final ErrorCode errorCode;
    private final String message;

    public RestApiException(ErrorCode errorCode, Object... args) {
        this.errorCode = errorCode;
        this.message = errorCode instanceof UserError ? ((UserError) errorCode).getFormattedMessage(args) : errorCode.getMessage();
    }
}
