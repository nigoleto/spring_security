package io.security.springsecuritymaster.jwt;

import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws IOException, ServletException {
        // 요청 헤더에서 Authorization 정보 추출
        String header = request.getHeader("Authorization");

        // Authorization 헤더가 존재하고 "Bearer "로 시작하는 경우 처리
        if (header != null && header.startsWith("Bearer ")) {
            String token = header.substring(7); // "Bearer " 이후의 토큰 부분 추출
            try {
                // JWT 토큰 검증 및 클레임 추출
                Claims claims = JwtUtil.validateToken(token);
                String username = claims.getSubject(); // 토큰의 사용자 이름
                String role = claims.get("role", String.class); // 토큰의 사용자 권한

                // Spring Security 인증 객체 생성
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                        username, null, Collections.singletonList(new SimpleGrantedAuthority(role)));
                SecurityContextHolder.getContext().setAuthentication(authentication); // 인증 정보 설정
            } catch (Exception e) {
                // 토큰이 유효하지 않을 경우, 401 에러 반환
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.getWriter().write("Invalid JWT token");
                return;
            }
        }

        // 필터 체인의 다음 필터로 요청 전달
        filterChain.doFilter(request, response);
    }
}
