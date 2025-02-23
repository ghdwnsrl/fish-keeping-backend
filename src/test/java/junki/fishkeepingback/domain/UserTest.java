package junki.fishkeepingback.domain;

import junki.fishkeepingback.global.config.security.Role;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

@Slf4j
public class UserTest {
    @Test
    void test() {
        log.info("here {}",Role.ROLE_ADMIN);
        Role role = Role.ROLE_ADMIN;
        log.info("role.name() {}",role.name());
    }
}
