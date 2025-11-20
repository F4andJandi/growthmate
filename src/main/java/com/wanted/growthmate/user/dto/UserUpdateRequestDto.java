package com.wanted.growthmate.user.dto;

import jakarta.validation.constraints.Email;
import lombok.Getter;

@Getter
public class UserUpdateRequestDto {

    @Email(message = "유효한 메일 형식이 아닙니다.")
    private String email;
}
