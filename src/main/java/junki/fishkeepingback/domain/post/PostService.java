package junki.fishkeepingback.domain.post;

import junki.fishkeepingback.domain.post.dao.PostRepository;
import junki.fishkeepingback.domain.post.dto.PostReq;
import junki.fishkeepingback.domain.user.User;
import junki.fishkeepingback.domain.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class PostService {
    private final PostRepository postRepository;
    private final UserService userService;

    public Long create(PostReq postReq, String username) {
        User user = userService.findByUsername(username);
        Post post = postReq.toEntity();
        post.addUser(user);
        return postRepository.save(post).getId();
    }

}
