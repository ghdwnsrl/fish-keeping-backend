package junki.fishkeepingback.domain.user.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record UserUpdateReq(String introText,
                            ProfileImageReq profileImage
) { }
