package com.wanted.growthmate.user.interceptor;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;


@Configuration
@RequiredArgsConstructor
public class WebConfiguration implements WebMvcConfigurer {

    private final LoginCheckInterceptor loginCheckInterceptor;
    private final AuthorizationInterceptor authorizationInterceptor;
    private final LoginUserArgumentResolver loginUserArgumentResolver;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {


        //로그인 체크
        registry.addInterceptor(loginCheckInterceptor)
                .order(1)
                .addPathPatterns("/**")
                .excludePathPatterns(
                        "/",         // 1. 첫 페이지 (index.html)
                        "/login",    // 2. 로그인 페이지 (login.html)
                        "/signup",   // 3. 회원가입 페이지 (signup.html)
                        "/signup-success",

                        "/api/users/login",
                        "/api/users/signup",

                        "/css/**",
                        "/js/**",
                        "/images/**",
                        "/error");
        //권한 체크 (강사, 수강생)
        registry.addInterceptor(authorizationInterceptor)
                .order(2)
                .addPathPatterns("/api/courses/**")
                .excludePathPatterns(
                        "/",
                        "/api/login",
                        "/api/signup",
                        "/css/**",
                        "/js/**",
                        "/images/**",
                        "/api/users/signup",
                        "/api/users/login",
                        "/api/users/signup",
                        "/api/users/login");
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(loginUserArgumentResolver);
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/api/**") // API 요청에 대해서만
                .allowedOrigins("http://localhost:3000") // 프론트엔드가 실행되는 출처
                .allowedMethods("GET", "POST", "PATCH", "DELETE", "OPTIONS")
                .allowCredentials(true) // ◀◀◀ 이게 가장 중요합니다 (쿠키 허용)
                .maxAge(3600);
    }
}
