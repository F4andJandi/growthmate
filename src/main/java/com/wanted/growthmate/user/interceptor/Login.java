package com.wanted.growthmate.user.interceptor;


import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/**
 * 컨트롤러 메소드의 파라미터에 이 어노테이션을 붙이면
 * 세션에서 로그인한 유저의 정보를 찾아 AuthUser DTO에 담아 주입해줍니다.
 */
@Target(ElementType.PARAMETER) //파라미터 전용
@Retention(RetentionPolicy.RUNTIME)
public @interface Login {
}
