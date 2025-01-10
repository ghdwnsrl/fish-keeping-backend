package junki.fishkeepingback.domain.user;

import junki.fishkeepingback.domain.user.dto.UserInfoRes;
import junki.fishkeepingback.domain.user.dto.UserUpdateReq;
import junki.fishkeepingback.global.Result;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    @GetMapping
    public ResponseEntity<Result> getUserInfo(@RequestParam("username") String username) {
        UserInfoRes userInfo = userService.getUserInfo(username);
        Result result = Result.builder()
                .data(userInfo)
                .status(HttpStatus.OK.value())
                .build();
        return ResponseEntity.ok(result);
    }

    @DeleteMapping
    public ResponseEntity<Result> deleteUserInfo(@AuthenticationPrincipal UserDetails userDetails) {
        userService.delete(userDetails);
        return ResponseEntity.ok().build();
    }

    @PutMapping
    public ResponseEntity<Result> updateUserInfo(@AuthenticationPrincipal UserDetails userDetails, @RequestBody UserUpdateReq userInfo) {
        log.info("Updating user info: {}", userInfo);
        userService.update(userDetails, userInfo);
        return ResponseEntity.ok().build();
    }
}
