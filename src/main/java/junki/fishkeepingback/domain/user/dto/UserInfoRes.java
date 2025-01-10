package junki.fishkeepingback.domain.user.dto;

public record UserInfoRes(
        String username,
        String profileImageUrl,
        String introText,
        boolean isDeleted
) { }
