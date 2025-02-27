package junki.fishkeepingback.global.config.security;

import junki.fishkeepingback.domain.user.error.UserError;
import junki.fishkeepingback.global.LoginAttemptService;
import junki.fishkeepingback.global.error.RestApiException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.Map;

@RequiredArgsConstructor
public class CustomUserAuthenticationProvider extends DaoAuthenticationProvider {

    private final LoginAttemptService loginAttemptService;
    private final Map<LoginType, UserDetailsService> userDetailsServiceMap;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        CustomAuthenticationToken customAuthenticationToken = (CustomAuthenticationToken) authentication;
        UserDetailsService userDetailsService = userDetailsServiceMap.get(customAuthenticationToken.getLoginType());
        super.setUserDetailsService(userDetailsService);
        return super.authenticate(authentication);
    }

    @Override
    protected void additionalAuthenticationChecks(UserDetails userDetails, UsernamePasswordAuthenticationToken authentication) throws AuthenticationException {

        CustomAuthenticationToken customAuthenticationToken = (CustomAuthenticationToken) authentication;

        UserDetailsService userDetailsService = userDetailsServiceMap.get(customAuthenticationToken.getLoginType());
        super.setUserDetailsService(userDetailsService);
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