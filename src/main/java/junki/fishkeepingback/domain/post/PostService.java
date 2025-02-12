package junki.fishkeepingback.domain.post;

import junki.fishkeepingback.domain.archive.Archive;
import junki.fishkeepingback.domain.post.dao.PostRepository;
import junki.fishkeepingback.domain.post.dto.PostReq;
import junki.fishkeepingback.domain.post.dto.PostRes;
import junki.fishkeepingback.domain.post.dto.PostSearchParam;
import junki.fishkeepingback.domain.post.error.PostError;
import junki.fishkeepingback.domain.user.User;
import junki.fishkeepingback.global.error.RestApiException;
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

    @Transactional
    public Post create(PostReq postReq, Archive archive, User user) {
        Post post = Post.builder()
                .title(postReq.title())
                .content(postReq.content())
                .archive(archive)
                .user(user)
                .views(0)
                .build();
        post.addUser(user);
        return postRepository.save(post);
    }

    @Transactional(readOnly = true)
    public Post findById(Long postId) {
        return postRepository.findById(postId)
                .orElseThrow(() -> new RestApiException(PostError.POST_NOT_FOUND));
    }

    @Transactional(readOnly = true)
    public Page<PostRes> getPosts(PageRequest pageRequest, String username, String archiveName, PostSearchParam postSearchParam) {
        return postRepository.findByUsername(username, archiveName, pageRequest, postSearchParam);
    }

    @Transactional
    public Post get(Long postId) {
        return findById(postId);
    }

    public void delete(Post post, String username) {
        if (isOwner(username, post))
            postRepository.deleteById(post.getId());
    }

    @Transactional
    public Post update(PostReq updatePostDto, Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RestApiException(PostError.POST_NOT_FOUND));
        post.update(
                updatePostDto.title(),
                updatePostDto.content()
        );
        return post;
    }

    private boolean isOwner(String username, Post post) {
        return post.getUser().getUsername().equals(username);
    }

    public List<PostRes> getPopularPost() {
        return postRepository.findByTop3();
    }

    public List<Post> findByArchiveName(String archiveName, String username) {
        return postRepository.findByArchiveNameAndUserUsername(archiveName, username);
    }
}
