package junki.fishkeepingback.domain.post;

import junki.fishkeepingback.domain.comment.CommentService;
import junki.fishkeepingback.domain.comment.dto.CommentRes;
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

import java.util.List;

@Service
@RequiredArgsConstructor
public class PostService {
    private final PostRepository postRepository;
    private final UserService userService;
    private final CommentService commentService;

    @Transactional
    public Long create(PostReq postReq, String username) {
        User user = userService.findByUsername(username);
        Post post = postReq.toEntity();
        post.addUser(user);
        return postRepository.save(post).getId();
    }

    @Transactional(readOnly = true)
    public Post findById(Long postId) {
        return postRepository.findById(postId)
                .orElseThrow(() -> new PostNotFound("존재하지 않는 게시글입니다."));
    }

    @Transactional(readOnly = true)
    public Page<PostRes> getPosts(PageRequest pageRequest) {
        return postRepository.findAll(pageRequest)
                .map(PostRes::new);
    }

    @Transactional(readOnly = true)
    public PostDetailRes get(Long postId) {
        List<CommentRes> comments = commentService.findByPostId(postId);
        Post post = findById(postId);
        return new PostDetailRes(post, comments);
    }
}
