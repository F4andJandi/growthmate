package com.wanted.growthmate.user.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean //스프링 시큐리티 비활성화 -> 하지 않으면 모든 요청을 막음
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                // 1. CSRF 보호 비활성화 (세션 방식을 사용하지만, API 서버로 가정)
                .csrf(csrf -> csrf.disable())
                // 2. 폼 로그인 비활성화 (우리가 직접 /login 엔드포인트를 만듦)
                .formLogin(formLogin -> formLogin.disable())
                // 3. HTTP 기본 인증 비활성화
                .httpBasic(httpBasic -> httpBasic.disable())
                // 4. 모든 HTTP 요청을 허용 (인증/인가를 컨트롤러에서 수동 처리)
                .authorizeHttpRequests(authz -> authz
                        .anyRequest().permitAll()
                );

        return http.build();
    }
}
