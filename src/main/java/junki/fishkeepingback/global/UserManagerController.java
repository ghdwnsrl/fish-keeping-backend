package junki.fishkeepingback.global;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import junki.fishkeepingback.domain.user.UserService;
import junki.fishkeepingback.domain.user.dto.JoinReq;
import junki.fishkeepingback.domain.user.dto.LoginReq;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequiredArgsConstructor
public class UserManagerController {

    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final HttpSessionSecurityContextRepository securityContextRepository = new HttpSessionSecurityContextRepository();

    @PostMapping("/api/login")
    public ResponseEntity<Void> login(@RequestBody LoginReq loginReq, HttpServletRequest request, HttpServletResponse response) {
        Authentication authenticationRequest =
                UsernamePasswordAuthenticationToken.unauthenticated(loginReq.username(), loginReq.password());
        Authentication authenticationResponse =
                this.authenticationManager.authenticate(authenticationRequest);
        SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
        securityContext.setAuthentication(authenticationResponse);
        securityContextRepository.saveContext(securityContext, request, response);

        return ResponseEntity.ok().build();
    }

    @PostMapping("/api/join")
    public ResponseEntity<Void> join(@RequestBody JoinReq joinReq) {
        userService.join(joinReq);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/api/test")
    public ResponseEntity<Void> test(HttpServletRequest request) {
        log.info("test session {}", request.getSession().toString());
        return ResponseEntity.ok().build();
    }
}
