package com.wanted.growthmate.user.interceptor;

import com.wanted.growthmate.user.dto.AuthUser;
import com.wanted.growthmate.user.role.Role;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

@Component
public class LoginUserArgumentResolver implements HandlerMethodArgumentResolver {

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        // @Login 사용하고 있는지?
        boolean hasLoginAnnotation = parameter.hasParameterAnnotation(Login.class);
        // 파라미터 타입 AuthUser.class 인지?
        boolean hasAuthUserType = AuthUser.class.isAssignableFrom(parameter.getParameterType());

        return hasLoginAnnotation && hasAuthUserType;
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
                                  NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {

        HttpServletRequest request = (HttpServletRequest) webRequest.getNativeRequest();
        HttpSession session = request.getSession(false);

        // LoginCheckInterceptor가 먼저 실행되므로 세션이 null일 순 없지만, 방어 코드
        if (session == null) {
            // (보통은 LoginCheckInterceptor에서 401을 보내므로 여기까지 오지 않습니다.)
            return null;
        }

        // 세션에서 ID와 Role을 가져옵니다. (UserController의 login 메소드에서 저장한 값)
        Long userId = (Long) session.getAttribute("loginUserId");
        Role userRole = (Role) session.getAttribute("loginUserRole");

        // AuthUser DTO를 생성하여 반환
        return new AuthUser(userId, userRole);
    }
}
