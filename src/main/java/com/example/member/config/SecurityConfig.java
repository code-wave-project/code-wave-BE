package com.example.member.config;

import com.example.member.filter.JwtAuthenticationFilter;
import com.example.member.util.JwtUtil;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final JwtUtil jwtUtil;

    public SecurityConfig(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();  // 비밀번호 암호화 방식 설정
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf().disable()  // API에서는 CSRF 보호 비활성화
                .authorizeHttpRequests()  // HTTP 요청에 대한 권한 설정 시작
                .requestMatchers(
                        "/api/members/register",
                        "/api/members/login",
                        "/api/members/forgot-password",
                        "/api/members/reset-password",
                        "/api/members/find-username",
                        "/swagger-ui/**",
                        "/v3/api-docs/**"
                ).permitAll() // 인증 없이 접근 가능
                .anyRequest().authenticated() // 나머지 요청은 인증 필요
                .and()
                .addFilterBefore(new JwtAuthenticationFilter(jwtUtil), UsernamePasswordAuthenticationFilter.class) // 🔥 JWT 필터 추가
                .formLogin().disable()  // 기본 로그인 폼 비활성화
                .httpBasic().disable();  // HTTP 기본 인증 비활성화 (API 사용 시 필요)
        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }
}
