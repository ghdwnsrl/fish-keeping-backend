package junki.fishkeepingback.global.config.security;

import junki.fishkeepingback.domain.user.error.UserError;
import junki.fishkeepingback.global.error.RestApiException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;

public class CustomAuthenticationProvider extends DaoAuthenticationProvider {
    @Override
    protected void additionalAuthenticationChecks(UserDetails userDetails, UsernamePasswordAuthenticationToken authentication) throws AuthenticationException {
        try {
            super.additionalAuthenticationChecks(userDetails, authentication);
        } catch (BadCredentialsException e) {
            throw new RestApiException(UserError.PASSWORD_MISMATCH);
        }
    }
}