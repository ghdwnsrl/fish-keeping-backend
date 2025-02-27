package junki.fishkeepingback.domain.user.dto;

import junki.fishkeepingback.global.config.security.LoginType;

public record LoginReq(
        String username,
        String password,
        LoginType loginType
) {}
