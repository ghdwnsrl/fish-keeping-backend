package junki.fishkeepingback.domain.user;

import lombok.Data;

@Data
public class JoinReq {
    private String username;
    private String password;
    private String confirmPassword;
}
