package junki.fishkeepingback.global;

import junki.fishkeepingback.domain.post.dao.PostRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;
import java.util.Optional;
import java.util.Set;

@Slf4j
@RequiredArgsConstructor
@Service
public class ViewCountService {

    private final StringRedisTemplate redisTemplate;
    private final PostRepository postRepository;

    public void incrementViewCount(Long postId) {
        ValueOperations<String, String> ops = redisTemplate.opsForValue();
        String key = "post:"+postId+":view_count";
        ops.increment(key);
    }

    @Transactional
    @Scheduled(cron = "0 */3 * * * ?")
    public void updateViewCounts() {
        Set<String> keys = redisTemplate.keys("post:*:view_count");
        Objects.requireNonNull(keys).forEach(this::updatePostViews);
    }

    private void updateDatabaseViewCount(Long postId, Long viewCount) {
        postRepository.updateViewCount(postId, viewCount);
    }

    private void updatePostViews(String key) {
        Optional.ofNullable(redisTemplate.opsForValue().get(key))
                .map(Long::parseLong)
                .ifPresent(viewCount -> {
                    Long postId = Long.parseLong(key.split(":")[1]);
                    updateDatabaseViewCount(postId, viewCount);
                    redisTemplate.delete(key);
                });
    }
}
