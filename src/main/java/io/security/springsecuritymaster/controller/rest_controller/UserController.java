package io.security.springsecuritymaster.controller.rest_controller;

import io.security.springsecuritymaster.domain.user.CreateUserRequestDto;
import io.security.springsecuritymaster.domain.user.LoginUserRequestDto;
import io.security.springsecuritymaster.domain.user.User;
import io.security.springsecuritymaster.jwt.JwtUtil;
import io.security.springsecuritymaster.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
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
}
