package com.wanted.growthmate.user.dto;

import com.wanted.growthmate.user.role.Role;

public class AuthUser {

    private Long id;
    private Role role;

    public AuthUser(Long id, Role role) {
        this.id = id;
        this.role = role;
    }

    public Long getId() {
        return id;
    }

    public Role getRole() {
        return role;
    }
}
