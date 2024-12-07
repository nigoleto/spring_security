package io.security.springsecuritymaster.controller.rest_controller;

import io.security.springsecuritymaster.domain.user.CreateUserRequestDto;
import io.security.springsecuritymaster.domain.user.LoginUserRequestDto;
import io.security.springsecuritymaster.domain.user.User;
import io.security.springsecuritymaster.jwt.JwtUtil;
import io.security.springsecuritymaster.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final JwtUtil jwtUtil;

    @PostMapping("/signup")
    public ResponseEntity<Void> signup(@Valid @RequestBody CreateUserRequestDto createUserRequestDto) {
        userService.createUser(createUserRequestDto);
        return new ResponseEntity<>(HttpStatusCode.valueOf(201));
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginUserRequestDto loginRequest) {
        User user = userService.validateUser(loginRequest.email(), loginRequest.password());
        // 로그인 성공 시 JWT 토큰 생성
        String token = jwtUtil.generateToken(user.getEmail(), user.getRole().getAuthority());
        return ResponseEntity.ok(Map.of("token", "Bearer " + token));
    }

    @GetMapping("/token")
    public ResponseEntity<Void> getToken(HttpServletRequest request, HttpServletResponse response) {
        // 현재 인증된 사용자 가져오기
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.isAuthenticated()) {
            OAuth2AuthenticationToken authToken = (OAuth2AuthenticationToken) authentication;
            Map<String, Object> attributes = authToken.getPrincipal().getAttributes();
            String registrationId = authToken.getAuthorizedClientRegistrationId();

            String email;
            String nickname;

//            email = (String) ((Map<String, Object>) attributes.get("kakao_account")).get("email");
//            nickname = (String) ((Map<String, Object>) attributes.get("properties")).get("nickname");
            if ("kakao".equals(registrationId)) {
                email = (String) ((Map<String, Object>) attributes.get("kakao_account")).get("email");
                nickname = (String) ((Map<String, Object>) attributes.get("properties")).get("nickname");
            } else if ("google".equals(registrationId)) {
                email = (String) attributes.get("email");
                nickname = (String) attributes.get("name");
            } else {
                return ResponseEntity.badRequest().build();
            }


            String role = authentication.getAuthorities().stream()
                    .map(GrantedAuthority::getAuthority)
                    .findFirst()
                    .orElse("ROLE_USER"); // 기본 역할


            // 외부api로그인 한 유저의 이메일이 새로운 이메일이라면 유저를 저장시킴
            if(!userService.isExistUserByEmail(email)) {
                userService.createApiUser(email, nickname);
            }

            // JWT 토큰 생성
            String token = jwtUtil.generateToken(email, role);
            // 응답 헤더에 토큰 추가
            response.setHeader("Authorization", "Bearer " + token);
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }
}
