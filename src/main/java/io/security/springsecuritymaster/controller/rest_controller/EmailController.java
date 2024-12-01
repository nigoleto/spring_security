package io.security.springsecuritymaster.controller.rest_controller;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.security.springsecuritymaster.jwt.JwtUtil;
import io.security.springsecuritymaster.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/verify-email")
@RequiredArgsConstructor
public class EmailController {

    private final UserService userService;

    @PostMapping
    public ResponseEntity<String> verifyEmail(@RequestParam String token) {
        try {
            Claims claims = JwtUtil.validateToken(token);

            // 토큰에서 이메일 인증 여부 확인
            boolean isEmailVerification = claims.get("isEmailVerification", Boolean.class);
            if (!isEmailVerification) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("유효하지 않은 이메일 인증 토큰입니다.");
            }

            // 이메일 인증 처리 (DB 업데이트 등)
            String email = claims.getSubject();
            userService.markEmailAsVerified(email); // 이메일 인증 상태 업데이트

            return ResponseEntity.ok("이메일 인증이 완료되었습니다.");
        } catch (ExpiredJwtException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("토큰이 만료되었습니다.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("유효하지 않은 토큰입니다.");
        }
    }
}
