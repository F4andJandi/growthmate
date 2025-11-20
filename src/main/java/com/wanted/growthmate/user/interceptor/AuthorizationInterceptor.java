package com.wanted.growthmate.user.interceptor;


import com.wanted.growthmate.user.role.Role;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class AuthorizationInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        //컨트롤러 메서드가 handlerMethod가 맞는지 확인
        if(!(handler instanceof HandlerMethod)) {
            return  true;
        }
        HandlerMethod handlerMethod = (HandlerMethod) handler;

        AuthRequired authRequired = handlerMethod.getMethodAnnotation(AuthRequired.class);

        if(authRequired == null) {
            return true;
        }
        //필요한 권한 가져오기
        Role requiredRole = authRequired.role();

        HttpSession session = request.getSession(false);
        Role userRole = (Role)  session.getAttribute("loginUserRole");
        //어드민이 추가되면 어드민이 강사의 권한도 가지게 되면 수정
        if(userRole == requiredRole || userRole == Role.ADMIN) {

            return true;
        }
        response.sendError(HttpServletResponse.SC_FORBIDDEN,"접근 권한이 없습니다.");
        return  false;
    }
}
