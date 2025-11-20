package com.wanted.growthmate.user.controller;


import com.wanted.growthmate.user.dto.*;


import com.wanted.growthmate.user.interceptor.AuthRequired;
import com.wanted.growthmate.user.interceptor.Login;
import com.wanted.growthmate.user.role.Role;
import com.wanted.growthmate.user.service.UserService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/signup")
    public ResponseEntity<UserResponseDto> createUser(@Valid @RequestBody UserSignUpRequestDto requestDto) {

        UserResponseDto responseDto = userService.signUp(requestDto);

        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
    }

    @PostMapping("/login")
    public ResponseEntity<UserResponseDto> login(
            @Valid @RequestBody UserLoginRequestDto requestDto, HttpSession session) {

        UserResponseDto responseDto = userService.login(requestDto);

        session.setAttribute("loginUserId", responseDto.getId());
        session.setAttribute("loginUserRole", responseDto.getRole());
        session.setMaxInactiveInterval(3600);//세션 만료 1시간

        return ResponseEntity.ok(responseDto);
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(HttpSession session) {

        if (session != null) {
            session.invalidate();
        }
        return ResponseEntity.ok("로그아웃 되었습니다.");
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDto> findUserById(
            @PathVariable("id") Long id,
            @Login AuthUser authUser) {

        //본인 확인
        if (!authUser.getId().equals(id) && authUser.getRole() != Role.ADMIN) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
        }
        UserResponseDto responseDto = userService.findUserById(id);
        return ResponseEntity.ok(responseDto);
    }

    @PatchMapping("{id}")
    public ResponseEntity<UserResponseDto> updateUser(
            @PathVariable("id") Long id,
            @Valid @RequestBody UserUpdateRequestDto requestDto,
            @Login AuthUser authUser) {

        if (!authUser.getId().equals(id) && authUser.getRole() != Role.ADMIN) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
        }

        UserResponseDto responseDto = userService.updateUser(id, requestDto);
        return ResponseEntity.ok(responseDto); // 💡 수정된 DTO 반환
    }

    @PostMapping("/change-password")
    public ResponseEntity<String> changePassword(
            @Valid @RequestBody PasswordChangeRequestDto requestDto,
            @Login AuthUser authUser
    ) {
        userService.changePassword(authUser.getId(), requestDto);
        return ResponseEntity.ok("비밀번호가 성공적으로 변경되었습니다.");
    }

    @AuthRequired(role = Role.ADMIN)
    @DeleteMapping("{id}")
    public ResponseEntity<Void> deleteUser (@PathVariable("id") Long id){

        userService.deleteById(id);
        return ResponseEntity.ok().build();
    }


    @DeleteMapping("/withdraw")
    public ResponseEntity<String> withdrawAccount(
            @Login AuthUser authUser,
            HttpSession session) {

        // 1. 세션에서 "본인" ID를 가져옵니다.
        Long loginUserId = authUser.getId();
        // 2. "본인" ID로 DB에서 삭제
        userService.deleteById(loginUserId);
        // 3. 세션을 만료시켜 즉시 로그아웃 처리
        session.invalidate();

        return ResponseEntity.ok("회원 탈퇴가 완료되었습니다.");
    }

}

