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

import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final UserService userService;

    @Transactional(readOnly = true)
    public Page<CommentRes> getCommentList(PageRequest pageRequest) {
        return commentRepository.findAll(pageRequest)
                .map(CommentRes::new);
    }

    @Transactional
    public void create(String username, Post post, @Valid CommentReq commentReq) {

        User writer = userService.findByUsername(username);
        Comment comment = Comment.builder()
                .user(writer)
                .content(commentReq.content())
                .build();

        comment.addPost(post);

        commentRepository.save(comment);
    }

    public List<CommentRes> findByPostId(Long postId) {
        return commentRepository.findByPostId(postId)
                .stream().map(CommentRes::new)
                .toList();
    }
}
