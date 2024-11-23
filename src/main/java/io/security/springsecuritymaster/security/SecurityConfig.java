package io.security.springsecuritymaster.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final CustomAuthenticationProvider provider;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http)
            throws Exception {
        return http.csrf(AbstractHttpConfigurer::disable).cors(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(api -> api
                        .requestMatchers("/css/**", "/js/**", "/images/**", "/static/**", "/api/**").permitAll()
                        .requestMatchers("/signup", "/login", "/", "/forgot-password").permitAll()
                        .requestMatchers("/api/gwangju/a").hasAnyRole("ADMIN")
                        .requestMatchers("/my")
                        .authenticated()
                        .anyRequest().hasAnyRole("USER", "ADMIN"))
                .formLogin(form -> form
//                        .loginPage("/login")
                        .loginProcessingUrl("/login")
                        .defaultSuccessUrl("/", true)
//                        .successHandler(successHandler)
//                        .failureHandler(failureHandler)
                        .permitAll()
                )
                .logout(logout -> logout.logoutUrl("/logout"))
                .authenticationProvider(provider)
//                .exceptionHandling(exceptions -> exceptions
//                        .authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED))
//                        .authenticationEntryPoint((request, response, authException) -> {
//                            response.sendRedirect("/login");
//                        })
//                        .accessDeniedHandler((request, response, accessDeniedException) -> {
//                            response.setStatus(HttpStatus.FORBIDDEN.value());
//                            response.getWriter().write("접근 권한이 없습니다.");
//                        })
//                )
                .build();

    }
    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }
}
