package junki.fishkeepingback.domain.user;

import junki.fishkeepingback.domain.user.dto.JoinReq;
import junki.fishkeepingback.domain.user.error.JoinError;
import junki.fishkeepingback.global.error.RestApiException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@RequiredArgsConstructor
@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

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

        String encodedPw = passwordEncoder.encode(joinReq.password());
        User user = new User(joinReq.username(), encodedPw);
        userRepository.save(user);
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
}
