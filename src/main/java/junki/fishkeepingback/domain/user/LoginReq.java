package junki.fishkeepingback.domain.user;

import lombok.Data;

@Data
public class LoginReq {
    private String username;
    private String password;
}
