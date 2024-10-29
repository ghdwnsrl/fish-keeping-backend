package junki.fishkeepingback.global;

import lombok.Builder;
import lombok.Data;

@Data
public class Result {
    private int status;
    private String message;
    private Object data;

    @Builder
    public Result(int status, String message, Object data) {
        this.status = status;
        this.message = message;
        this.data = data;
    }
}
