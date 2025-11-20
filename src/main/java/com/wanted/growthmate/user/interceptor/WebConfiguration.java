package com.wanted.growthmate.user.interceptor;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
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
                .addPathPatterns("/api/**")
                .excludePathPatterns(
                        "/",         // 1. 첫 페이지 (index.html)
                        "/api/login",    // 2. 로그인 페이지 (login.html)
                        "/api/signup",   // 3. 회원가입 페이지 (signup.html)
                        "/api/users/login",
                        "/api/users/signup",

                        "/api/users/login",
                        "/api/users/signup");
        //권한 체크 (강사, 수강생)
        registry.addInterceptor(authorizationInterceptor)
                .order(2)
                .addPathPatterns("/api/**")
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
}
