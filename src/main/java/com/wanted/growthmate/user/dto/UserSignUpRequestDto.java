package com.wanted.growthmate.user.dto;

import com.wanted.growthmate.user.role.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class UserSignUpRequestDto {


    @NotBlank(message = "아이디를 입력해주세요.")
    private String username;


    @NotBlank(message = "패스워드를 입력해주세요.")
    private String password;


    @NotNull(message = "역할을 선택해주세요.")
    private Role role;


}
