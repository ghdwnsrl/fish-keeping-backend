package junki.fishkeepingback.domain.user.dto;

import junki.fishkeepingback.domain.term.AgreeReq;

public record JoinReq(
        String username,
        String password,
        String confirmPassword,
        AgreeReq agreeReq
) {}
