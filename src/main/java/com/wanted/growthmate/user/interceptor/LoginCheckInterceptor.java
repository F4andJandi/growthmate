package com.wanted.growthmate.user.interceptor;


import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class LoginCheckInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request
            , HttpServletResponse response, Object handler) throws Exception {

        HttpSession session = request.getSession(false);

        if (session == null || session.getAttribute("loginUserId") == null) {
            // MVC 컨트롤러인 경우 로그인 페이지로 리다이렉트
            String requestURI = request.getRequestURI();
            if (requestURI.startsWith("/points") || requestURI.startsWith("/myc") || 
                requestURI.startsWith("/sections") || (requestURI.startsWith("/courses") && requestURI.contains("/enroll"))) {
                response.sendRedirect("/login");
                return false;
            }
            
            // API 요청인 경우 에러 응답
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "로그인이 필요합니다.");
            return false;
        }

        return true;

    }
}
