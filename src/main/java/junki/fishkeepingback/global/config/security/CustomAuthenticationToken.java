package junki.fishkeepingback.global.config.security;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

public class CustomAuthenticationToken extends UsernamePasswordAuthenticationToken {

    private final LoginType loginType;

    public CustomAuthenticationToken(Object principal, Object credentials, LoginType loginType) {
        super(principal, credentials);
        this.loginType = loginType;
    }

    public CustomAuthenticationToken(Object principal, Object credentials, Collection<? extends GrantedAuthority> authorities, LoginType loginType) {
        super(principal, credentials, authorities);
        this.loginType = loginType;
    }

    public LoginType getLoginType() {
        return loginType;
    }

    public static CustomAuthenticationToken unauthenticated(String username, String password, LoginType loginType) {
        return new CustomAuthenticationToken(username, password, loginType);
    }
}
