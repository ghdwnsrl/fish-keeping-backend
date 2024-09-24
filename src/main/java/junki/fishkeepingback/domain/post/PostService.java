package junki.fishkeepingback.domain.post;

import junki.fishkeepingback.domain.comment.exeception.PostNotFound;
import junki.fishkeepingback.domain.post.dao.PostRepository;
import junki.fishkeepingback.domain.post.dto.PostDetailRes;
import junki.fishkeepingback.domain.post.dto.PostReq;
import junki.fishkeepingback.domain.post.dto.PostRes;
import junki.fishkeepingback.domain.user.User;
import junki.fishkeepingback.domain.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PostService {
    private final PostRepository postRepository;
    private final UserService userService;

    @Transactional
    public Long create(PostReq postReq, String username) {
        User user = userService.findByUsername(username);
        Post post = postReq.toEntity();
        post.addUser(user);
        return postRepository.save(post).getId();
    }

    @Transactional(readOnly = true)
    public Optional<Post> findById(Long postId) {
        return postRepository.findById(postId);
    }

    @Transactional(readOnly = true)
    public Page<PostRes> getPosts(PageRequest pageRequest) {
        return postRepository.findAll(pageRequest)
                .map(PostRes::new);
    }

    @Transactional(readOnly = true)
    public PostDetailRes get(Long postId) {
        return this.findById(postId)
                .map(PostDetailRes::new)
                .orElseThrow(() -> new PostNotFound("게시글을 찾을 수 없습니다."));
    }
}
