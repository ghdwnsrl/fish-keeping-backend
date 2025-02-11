package junki.fishkeepingback.global;

import junki.fishkeepingback.domain.post.dao.PostRepository;
import junki.fishkeepingback.domain.post.dto.PostRes;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

@Slf4j
@RequiredArgsConstructor
@Service
public class ViewCountService {

    private final StringRedisTemplate redisTemplate;
    private final PostRepository postRepository;

    public Integer incrementViewCount(Long postId, Integer views) {
        ValueOperations<String, String> ops = redisTemplate.opsForValue();
        String key = "post:"+postId+":view_count";
        ops.setIfAbsent(key, String.valueOf(views));
        ops.increment(key);
        String result = ops.get(key);
        return Integer.valueOf(result);
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

    public List<String> getKeys(Page<PostRes> posts) {
        List<String> keys = posts.stream()
                .map(postDto -> "post:"+postDto.id()+":view_count")
                .toList();
        return redisTemplate.opsForValue().multiGet(keys);
    }
}
