package io.security.springsecuritymaster.security;

import io.security.springsecuritymaster.jwt.JwtAuthenticationFilter;
import io.security.springsecuritymaster.jwt.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.client.web.OAuth2LoginAuthenticationFilter;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;

import java.util.Collections;
import java.util.Map;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtUtil jwtUtil;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http)
            throws Exception {
        return http
                .csrf(AbstractHttpConfigurer::disable)
                .cors(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED))
                .authorizeHttpRequests(api -> api
                        .requestMatchers("/css/**", "/js/**", "/img/**", "/static/**").permitAll()
                        .requestMatchers("/api/login", "/api/signup" ,"/api/clothes", "/api/gwangju", "/api/token", "/api/validate-email").permitAll()
                        .requestMatchers("/clothes/**","/loginSuccess" , "/loginFailure", "/oauth2/authorization/**", "/login/oauth2/code/**").permitAll()
                        .requestMatchers("/signup", "/login", "/logout" , "/", "/forgot-password", "/verify-email").permitAll()
                        .requestMatchers("/api/gwangju/*").hasAuthority("ROLE_ADMIN")
                        .anyRequest().authenticated()
                )
                .formLogin(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)
                .logout(logout -> logout
                        .logoutUrl("/custom-logout") // Spring Security의 기본 로그아웃 URL을 덮어씀
                        .invalidateHttpSession(true)
                        .clearAuthentication(true)
                        .logoutSuccessUrl("/") // 필요하다면 성공 시 리다이렉트 설정 가능
                )
                .oauth2Login(oauth2 -> oauth2
//                        .loginProcessingUrl("/login/oauth2/code/*")
                        .userInfoEndpoint(userInfo -> userInfo
                                .userService(customOAuth2UserService()) // 사용자 정의 OAuth2UserService 등록
                        )
                                .successHandler((request, response, authentication) -> {
                                    // JWT 발급 및 설정
                                    OAuth2AuthenticationToken authToken = (OAuth2AuthenticationToken) authentication;

                                    // 사용자 정보 추출
                                    Map<String, Object> attributes = authToken.getPrincipal().getAttributes();

//                                    String email = (String) ((Map<String, Object>) attributes.get("kakao_account")).get("email"); // 이메일 필드 이름 확인 필요
//                                    String role = authToken.getAuthorities().stream()
//                                            .map(GrantedAuthority::getAuthority)
//                                            .findFirst()
//                                            .orElse("ROLE_USER"); // 기본 역할 설정
//
//                                    String token = jwtUtil.generateToken(email, role);

                                    // JWT를 HTTP 응답 헤더에 추가
//                                    response.setHeader("Authorization", "Bearer " + token);

                                    // 인증 객체 생성 및 SecurityContext에 설정
//                                    UsernamePasswordAuthenticationToken auth =
//                                            new UsernamePasswordAuthenticationToken(email, null, List.of(new SimpleGrantedAuthority(role)));
//                                    SecurityContextHolder.getContext().setAuthentication(auth);

                                    response.sendRedirect("/loginSuccess");
                                })
                                .failureHandler((request, response, exception) -> {
                                    // 로그로 에러 메시지 출력
                                    exception.printStackTrace();
                                    response.sendRedirect("/loginFailure");
                                })
                )
                .addFilterAfter(jwtAuthenticationFilter, OAuth2LoginAuthenticationFilter.class)
//                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class) // JWT 필터 등록
                .exceptionHandling(exceptions -> exceptions
                        .authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED))
                        .accessDeniedHandler((request, response, accessDeniedException) -> {
                            response.setStatus(HttpStatus.FORBIDDEN.value());
                            response.getWriter().write("접근 권한이 없습니다.");
                        })
                )
                .build();
    }

    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public OAuth2UserService<OAuth2UserRequest, OAuth2User> customOAuth2UserService() {
        return userRequest -> {
            // 기본 OAuth2UserService 호출
            OAuth2User oAuth2User = new DefaultOAuth2UserService().loadUser(userRequest);

            // 카카오 사용자 정보 가져오기
            Map<String, Object> attributes = oAuth2User.getAttributes();

            String registrationId = userRequest.getClientRegistration().getRegistrationId();

            // DefaultOAuth2User 생성 및 반환
            if(registrationId.equals("kakao")){
                return new DefaultOAuth2User(
                        Collections.singleton(new SimpleGrantedAuthority("ROLE_USER")), // 권한
                        attributes,
                        "id" // 사용자 이름 속성으로 사용할 키
                );
            } else if(registrationId.equals("google")) {
                return new DefaultOAuth2User(
                        Collections.singleton(new SimpleGrantedAuthority("ROLE_USER")), // 권한
                        attributes,
                        "email" // 사용자 이름 속성으로 사용할 키
                );
            } else {
                return new DefaultOAuth2User(
                        Collections.singleton(new SimpleGrantedAuthority("ROLE_USER")), // 권한
                        attributes,
                        "id" // 사용자 이름 속성으로 사용할 키
                );
            }
        };
    }
}
