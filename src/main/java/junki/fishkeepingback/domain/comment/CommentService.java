package junki.fishkeepingback.domain.comment;

import jakarta.validation.Valid;
import junki.fishkeepingback.domain.comment.dto.CommentReq;
import junki.fishkeepingback.domain.comment.dto.CommentRes;
import junki.fishkeepingback.domain.comment.exeception.PostNotFound;
import junki.fishkeepingback.domain.post.Post;
import junki.fishkeepingback.domain.post.PostService;
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
public class CommentService {

    private final CommentRepository commentRepository;
    private final UserService userService;
    private final PostService postService;

    @Transactional(readOnly = true)
    public Page<CommentRes> getCommentList(PageRequest pageRequest) {
        return commentRepository.findAll(pageRequest)
                .map(CommentRes::new);
    }

    @Transactional
    public void create(String username, @Valid CommentReq commentReq) {

        User writer = userService.findByUsername(username);
        Post post = postService.findById(commentReq.postId())
                .orElseThrow(() -> new PostNotFound("존재하지 않는 게시글입니다."));

        Comment comment = Comment.builder()
                .user(writer)
                .content(commentReq.content())
                .post(post)
                .build();

        commentRepository.save(comment);
    }
}
