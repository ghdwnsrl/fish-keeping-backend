package junki.fishkeepingback.global;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Slf4j
@Service
public class LoginAttemptService {
    private static final int MAX_ATTEMPTS = 5;
    private static final Duration LOCK_TIME = Duration.ofMinutes(10);

    private final StringRedisTemplate redisTemplate;

    public LoginAttemptService(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public long loginFailed(String username) {
        ValueOperations<String, String> ops = redisTemplate.opsForValue();
        String key = "login:fail:" + username;
        Long attempts = ops.increment(key);
        if (attempts == 1) {
            redisTemplate.expire(key, LOCK_TIME);
        }
        return MAX_ATTEMPTS - attempts;
    }

    public boolean isBlocked(String username) {
        String key = "login:fail:" + username;
        String attempts = redisTemplate.opsForValue().get(key);
        return attempts != null && Integer.parseInt(attempts) >= MAX_ATTEMPTS;
    }

    public void loginSucceeded(String username) {
        String key = "login:fail:" + username;
        redisTemplate.delete(key);
    }
}
