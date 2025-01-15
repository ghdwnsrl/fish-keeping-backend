package junki.fishkeepingback.domain.user.dto;

public record ProfileImageReq(
        String profileImageUrl,
        String resizedProfileImageUrl
) { }
