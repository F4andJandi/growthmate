package com.wanted.growthmate.user.dto;

import com.wanted.growthmate.user.entity.User;
import com.wanted.growthmate.user.role.Role;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class UserResponseDto {

    private final Long id;
    private final String username;
    private final String email;
    private final Role role;
    private final LocalDateTime createdAt;

    //  User 엔티티를 DTO로 변환 비밀번호는 제외
    public UserResponseDto(User user) {
        this.id = user.getId();
        this.username = user.getUsername();
        this.email = user.getEmail();
        this.role = user.getRole();
        this.createdAt = user.getCreatedAt();
    }
}
