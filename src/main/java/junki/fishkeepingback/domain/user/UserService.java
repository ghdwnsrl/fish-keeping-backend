package junki.fishkeepingback.domain.user;

import junki.fishkeepingback.domain.archive.Archive;
import junki.fishkeepingback.domain.image.uploader.S3Uploader;
import junki.fishkeepingback.domain.term.AgreeReq;
import junki.fishkeepingback.domain.user.dto.JoinReq;
import junki.fishkeepingback.domain.user.dto.ProfileImageReq;
import junki.fishkeepingback.domain.user.dto.UserInfoRes;
import junki.fishkeepingback.domain.user.dto.UserUpdateReq;
import junki.fishkeepingback.domain.user.error.JoinError;
import junki.fishkeepingback.domain.userterm.UserTermService;
import junki.fishkeepingback.global.config.security.Role;
import junki.fishkeepingback.global.error.CommonErrorCode;
import junki.fishkeepingback.global.error.RestApiException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Slf4j
@Transactional
@RequiredArgsConstructor
@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserTermService userTermService;
    private final S3Uploader s3Uploader;

    @Value(value = "${baseUrl.userThumbnailUrl}")
    private String profileImageUrl;

    public void join(JoinReq joinReq) {
        if (isDuplicate(joinReq.username())) {
            throw new RestApiException(JoinError.DUPLICATE_USERNAME);
        }
        if (!isValidPassword(joinReq)) {
            throw new RestApiException(JoinError.INVALID_PASSWORD);
        }
        if (!isConfirmPasswordMatch(joinReq)) {
            throw new RestApiException(JoinError.CONFIRM_PASSWORD_MISMATCH);
        }
        if (!isAgree(joinReq)) {
            throw new RestApiException(JoinError.NOT_INVALID_AGREE);
        }

        String encodedPw = passwordEncoder.encode(joinReq.password());
        User user = new User(joinReq.username(), encodedPw, profileImageUrl, Role.ROLE_USER);
        Archive archive = new Archive("선택 안함", user);
        archive.addUser(user);
        userTermService.save(user);
        userRepository.save(user);

    }

    private static Boolean isAgree(JoinReq joinReq) {
        AgreeReq agreeReq = joinReq.agreeReq();
        return (agreeReq.ageAgree() && agreeReq.privacyAgree() && agreeReq.termsAgree());
    }

    private boolean isValidPassword(JoinReq joinReq) {
        String regex = "^(?=.*[a-zA-Z])(?=.*\\d)(?=.*[!@#$%^&*()])[a-zA-Z\\d!@#$%^&*()]{8,12}$";
        return joinReq.password().matches(regex);
    }

    private boolean isConfirmPasswordMatch(JoinReq joinReq) {
        return joinReq.confirmPassword()
                .equals(joinReq.password());
    }

    public boolean isDuplicate(String username) {
        return userRepository
                .findByUsername(username)
                .isPresent();
    }

    public User findByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(username));
    }

    public User findByUserDetails(UserDetails userDetails) {
        String username = getUsername(userDetails);
        return findByUsername(username);
    }

    public UserInfoRes getUserInfo(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(username));
        return new UserInfoRes(user.getUsername(), user.getProfileImageUrl(), user.getIntroText(), user.getIsDeleted());
    }

    private static String getUsername(UserDetails userDetails) {
        return Optional.ofNullable(userDetails)
                .map(UserDetails::getUsername)
                .orElseThrow(() -> new RestApiException(CommonErrorCode.RESOURCE_NOT_FOUND));
    }


    public void delete(UserDetails userDetails) {
        String username = getUsername(userDetails);
        User user = findByUsername(username);
        user.deleteSoft();
    }

    public void update(UserDetails userDetails, UserUpdateReq userInfo) {

        User user = findByUsername(userDetails.getUsername());

        Optional.ofNullable(userInfo)
                .map(UserUpdateReq::profileImage)
                .ifPresent(profileImageReq -> updateProfileImage(profileImageReq, user));

        Optional.ofNullable(userInfo)
                .map(UserUpdateReq::introText)
                .ifPresent(user::updateIntroText);
    }

    private void updateProfileImage(ProfileImageReq profileImageReq, User user) {
        String prevProfileImageUrl = user.getProfileImageUrl();
        String prevResizedProfileImageUrl = user.getResizedProfileImageUrl();

        if (prevProfileImageUrl != null && !prevProfileImageUrl.equals(profileImageUrl))
            s3Uploader.delete(prevProfileImageUrl);
        if (prevResizedProfileImageUrl != null)
            s3Uploader.delete(prevResizedProfileImageUrl);

        user.updateProfileImage(
                profileImageReq.profileImageUrl(),
                profileImageReq.resizedProfileImageUrl());
    }
}
