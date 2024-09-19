package junki.fishkeepingback.domain.user;

import lombok.RequiredArgsConstructor;
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
        String encodedPw = passwordEncoder.encode(joinReq.password());
        User user = new User(joinReq.username(), encodedPw);
        userRepository.save(user);
    }
}
