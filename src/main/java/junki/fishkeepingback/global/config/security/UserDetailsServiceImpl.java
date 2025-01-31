package junki.fishkeepingback.global.config.security;

import junki.fishkeepingback.domain.user.User;
import junki.fishkeepingback.domain.user.UserRepository;
import junki.fishkeepingback.domain.user.error.UserError;
import junki.fishkeepingback.global.error.RestApiException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
@RequiredArgsConstructor
@Slf4j
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) {

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RestApiException(UserError.USER_NOT_FOUND));

        Boolean isDeleted = user.getIsDeleted();

        if (isDeleted) {
            throw new RestApiException(UserError.USER_NOT_FOUND);
        }

        return new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPassword(),
                user.getAuthorities()
        );
    }
}
