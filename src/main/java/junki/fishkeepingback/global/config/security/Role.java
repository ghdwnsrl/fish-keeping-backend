package junki.fishkeepingback.global.config.security;

import java.util.Set;

public enum Role {
    ROLE_ADMIN(Set.of("ROLE_ADMIN", "ROLE_USER")),
    ROLE_USER(Set.of("ROLE_USER"));

    private final Set<String> authorities;

    Role(Set<String> authorities) {
        this.authorities = authorities;
    }

    public Set<String> getAuthorities() {
        return authorities;
    }
}
