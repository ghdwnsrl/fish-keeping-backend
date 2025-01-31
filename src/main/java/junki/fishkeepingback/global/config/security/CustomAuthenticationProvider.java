package junki.fishkeepingback.global.config.security;

import junki.fishkeepingback.domain.user.error.UserError;
import junki.fishkeepingback.global.LoginAttemptService;
import junki.fishkeepingback.global.error.RestApiException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;

@RequiredArgsConstructor
public class CustomAuthenticationProvider extends DaoAuthenticationProvider {

    private final LoginAttemptService loginAttemptService;

    @Override
    protected void additionalAuthenticationChecks(UserDetails userDetails, UsernamePasswordAuthenticationToken authentication) throws AuthenticationException {
        boolean isBlocked = loginAttemptService.isBlocked(userDetails.getUsername());
        if (isBlocked) throw new RestApiException(UserError.LOGIN_ATTEMPTS_EXCEEDED);

        try {
            super.additionalAuthenticationChecks(userDetails, authentication);
            loginAttemptService.loginSucceeded(userDetails.getUsername());
        } catch (BadCredentialsException e) {
            long remainAttempts = loginAttemptService.loginFailed(userDetails.getUsername());
            throw new RestApiException(UserError.PASSWORD_MISMATCH, remainAttempts);
        }
    }
}