package junki.fishkeepingback.domain.postlike;


import junki.fishkeepingback.domain.post.Post;
import junki.fishkeepingback.domain.post.PostService;
import junki.fishkeepingback.domain.user.User;
import junki.fishkeepingback.domain.user.UserService;
import junki.fishkeepingback.global.error.CommonErrorCode;
import junki.fishkeepingback.global.error.RestApiException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@RequiredArgsConstructor
@Transactional
@Service
public class PostLikeService {
    private final PostLikeRepository postLikeRepository;
    private final PostService postService;
    private final UserService userService;

    public void create(Long postId, UserDetails userDetails) {
        Post post = postService.findById(postId);
        User user = userService.findByUsername(userDetails.getUsername());
        PostLike build = new PostLike(user, post);
        postLikeRepository.save(build);
    }

    public void delete(Long postId, UserDetails userDetails) {
        String username = Optional.ofNullable(userDetails)
                .map(UserDetails::getUsername)
                .orElseThrow(() -> new RestApiException(CommonErrorCode.RESOURCE_NOT_FOUND));
        User user = userService.findByUsername(username);
        postLikeRepository.deleteByPostIdAndUserId(postId, user.getId());
    }
}
