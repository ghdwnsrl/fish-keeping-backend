package junki.fishkeepingback.domain.comment;

import jakarta.validation.Valid;
import junki.fishkeepingback.domain.comment.dao.CommentRepository;
import junki.fishkeepingback.domain.comment.dto.CommentReq;
import junki.fishkeepingback.domain.comment.dto.CommentRes;
import junki.fishkeepingback.domain.post.Post;
import junki.fishkeepingback.domain.user.User;
import junki.fishkeepingback.domain.user.UserService;
import junki.fishkeepingback.global.error.CommonErrorCode;
import junki.fishkeepingback.global.error.RestApiException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final UserService userService;

    @Transactional(readOnly = true)
    public Page<CommentRes> getCommentList(Long postId, PageRequest pageRequest) {
        return commentRepository.findCommentsByPostId(postId, pageRequest);
    }

    @Transactional
    public void create(String username, Post post, @Valid CommentReq commentReq) {

        User writer = userService.findByUsername(username);
        Comment comment = Comment.builder()
                .user(writer)
                .content(commentReq.content())
                .build();

        if (commentReq.parentId() != null) {
            Comment parent = commentRepository.findById(commentReq.parentId())
                    .orElseThrow(() -> new RestApiException(CommonErrorCode.RESOURCE_NOT_FOUND));
            parent.addComment(comment);
        }

        comment.addPost(post);

        commentRepository.save(comment);
    }

    @Transactional
    public void update(UserDetails userDetails, CommentReq updateCommentDto, Long commentId) {
        String username = userDetails.getUsername();
        commentRepository.findById(commentId)
                .ifPresentOrElse(
                        comment -> updateComment(updateCommentDto, comment, username),
                        () -> {throw new RestApiException(CommonErrorCode.ForbiddenOperationException);}
                );
    }

    private void updateComment(CommentReq updateCommentDto, Comment comment, String username) {
        if (!comment.getUser().getUsername().equals(username)) {
            throw new RestApiException(CommonErrorCode.ForbiddenOperationException);
        }
        comment.update(updateCommentDto.content());
    }

    @Transactional
    public void delete(Long commentId, String username) {
        commentRepository.findById(commentId)
                .ifPresent(comment -> {
                    if (comment.getUser().getUsername().equals(username)) {
                        commentRepository.delete(comment);
                    }
                });
    }

    @Transactional
    public void deleteByPostId(Long postId) {
        commentRepository.deleteChildCommentsByPostId(postId);
        commentRepository.deleteParentCommentsByPostId(postId);
    }
}
