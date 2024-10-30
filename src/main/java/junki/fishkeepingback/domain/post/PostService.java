package junki.fishkeepingback.domain.post;

import junki.fishkeepingback.domain.archive.Archive;
import junki.fishkeepingback.domain.archive.ArchiveService;
import junki.fishkeepingback.domain.comment.CommentService;
import junki.fishkeepingback.domain.comment.dto.CommentRes;
import junki.fishkeepingback.domain.comment.exception.PostNotFound;
import junki.fishkeepingback.domain.post.dao.PostRepository;
import junki.fishkeepingback.domain.post.dto.PostDetailRes;
import junki.fishkeepingback.domain.post.dto.PostReq;
import junki.fishkeepingback.domain.post.dto.PostRes;
import junki.fishkeepingback.domain.user.User;
import junki.fishkeepingback.domain.user.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class PostService {
    private final PostRepository postRepository;
    private final UserService userService;
    private final ArchiveService archiveService;
    private final CommentService commentService;

    @Transactional
    public Long create(PostReq postReq, String username) {
        User user = userService.findByUsername(username);
        Archive archive = archiveService.findByArchiveName(postReq.archiveName(), user);
        Post post = Post.builder()
                .title(postReq.title())
                .content(postReq.content())
                .archive(archive)
                .user(user)
                .views(0)
                .build();
        post.addUser(user);
        return postRepository.save(post).getId();
    }

    @Transactional(readOnly = true)
    public Post findById(Long postId) {
        return postRepository.findById(postId)
                .orElseThrow(() -> new PostNotFound("존재하지 않는 게시글입니다."));
    }

    @Transactional(readOnly = true)
    public Page<PostRes> getPosts(PageRequest pageRequest, String username, String archiveName) {
        return postRepository.findByUsername(username, archiveName, pageRequest);
    }

    @Transactional(readOnly = true)
    public PostDetailRes get(Long postId) {
        List<CommentRes> comments = commentService.findByPostId(postId);
        Post post = findById(postId);
        return new PostDetailRes(post, comments);
    }
}
