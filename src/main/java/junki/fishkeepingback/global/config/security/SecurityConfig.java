package junki.fishkeepingback.global.config.security;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import junki.fishkeepingback.global.LoginAttemptService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.security.web.authentication.logout.SimpleUrlLogoutSuccessHandler;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.web.cors.CorsUtils;

import java.util.Map;

import static junki.fishkeepingback.global.config.security.LoginType.*;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@Slf4j
class SecurityConfig {

    private final UserDetailsService userDetailsServiceImpl;
    private final UserDetailsService adminDetailsServiceImpl;
    private final LoginAttemptService loginAttemptService;

    @Bean
    public BCryptPasswordEncoder encoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .securityContext(securityContext -> securityContext.securityContextRepository(new HttpSessionSecurityContextRepository()))
                .csrf(AbstractHttpConfigurer::disable)
                .logout(logout -> logout.logoutUrl("/api/logout")
                        .logoutSuccessHandler(logoutSuccessHandler())
                )
                .authorizeHttpRequests(request -> request
                        .requestMatchers("/actuator/**").permitAll()
                        .requestMatchers("/api/admin/**").hasRole("ADMIN")
                        .requestMatchers(CorsUtils::isPreFlightRequest).permitAll()
                        .requestMatchers("/api/login", "/api/archives", "/api/join", "/api/session/validate").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/posts/*", "/api/posts", "/api/terms/*", "/api/*/comments", "/api/users").permitAll()
                        .anyRequest().authenticated()
                );

        return http.build();
    }

    @Bean
    public LogoutSuccessHandler logoutSuccessHandler() {
        return new SimpleUrlLogoutSuccessHandler() {
            @Override
            public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) {
                response.setStatus(HttpServletResponse.SC_OK);
            }
        };
    }

    @Bean
    public ProviderManager authenticationManager() {
        Map<LoginType, UserDetailsService> map = Map.of(
                ADMIN, adminDetailsServiceImpl,
                USER, userDetailsServiceImpl
        );
        DaoAuthenticationProvider provider = new CustomAuthenticationProvider(loginAttemptService, map);
        provider.setPasswordEncoder(encoder());
        return new ProviderManager(provider);
    }
}