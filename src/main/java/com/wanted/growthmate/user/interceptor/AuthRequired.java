package com.wanted.growthmate.user.interceptor;


import com.wanted.growthmate.user.role.Role;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface AuthRequired {
    Role role() default  Role.STUDENT;
}
