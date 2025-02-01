package junki.fishkeepingback.domain.postlike;


import junki.fishkeepingback.domain.post.Post;
import junki.fishkeepingback.domain.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@RequiredArgsConstructor
@Transactional
@Service
public class PostLikeService {
    private final PostLikeRepository postLikeRepository;

    public void create(Post post, User user) {
        PostLike postLike = new PostLike(user, post);
        postLikeRepository.save(postLike);
    }

    public void delete(Long postId, Long userId) {
        postLikeRepository.deleteByPostIdAndUserId(postId, userId);
    }

    public void deleteByPostId(Long postId) {
        postLikeRepository.deleteByPostId(postId);
    }

    public boolean getIsLiked(User user, Post post) {
        return Optional.ofNullable(user)
                .map(usr -> postLikeRepository.existsByPostAndUser(post, user))
                .orElse(false);
    }
}
