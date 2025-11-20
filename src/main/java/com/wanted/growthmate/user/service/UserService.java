package com.wanted.growthmate.user.service;


import com.wanted.growthmate.user.dto.*;
import com.wanted.growthmate.user.entity.User;
import com.wanted.growthmate.user.exception.UserAlreadyExistsException;
import com.wanted.growthmate.user.exception.UserNotFoundByIdException;
import com.wanted.growthmate.user.exception.UserWrongPasswordException;
import com.wanted.growthmate.user.repository.UserRepository;
import com.wanted.growthmate.user.role.Role;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;


@Service
@RequiredArgsConstructor
public class UserService {

    private final PasswordEncoder encoder;
    private final UserRepository userRepository;

    //로그인 메서드
    public UserResponseDto login(UserLoginRequestDto requestDto) {

        User user = userRepository.findByUsername(requestDto.getUsername())
                .orElseThrow(() -> new IllegalArgumentException("가입되지 않은 회원입니다."));

        if (!encoder.matches(requestDto.getPassword(), user.getPassword())) {
            throw new UserWrongPasswordException("비밀번호가 일치하지 않습니다.");
        }
        //엔티티를 DTO로 변환하고 반환
        return new UserResponseDto(user);
    }


    //회원 가입
    public UserResponseDto signUp(UserSignUpRequestDto requestDto) {

        if (userRepository.findByUsername(requestDto.getUsername()).isPresent()) {
            // 💡 [수정] 명확한 예외 발생
            throw new UserAlreadyExistsException("이미 사용 중인 아이디입니다.");
        }


        User user = User.builder()
                .username(requestDto.getUsername())
                .password(encoder.encode(requestDto.getPassword())) // 암호화
                .role(requestDto.getRole()) //
                .createdAt(LocalDateTime.now())
                .build();

        User signUp = userRepository.save(user); //user엔티티를 받음
        return new UserResponseDto(signUp);
    }
    //ID 조회
    public UserResponseDto findUserById(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(()->new UserNotFoundByIdException("해당아이디를 찾을 수 없습니다." + userId));

        return new UserResponseDto(user);
    }
    //ID 변경
    @Transactional
    public UserResponseDto updateUser(Long userId, UserUpdateRequestDto requestDto) {

        User user = userRepository.findById(userId)
                .orElseThrow(()-> new UserNotFoundByIdException("id 없음"));

        user.updateEmail(requestDto.getEmail());

        return new UserResponseDto(user);

    }
    //ID 삭제
    public void deleteById(Long userId) {
        userRepository.deleteById(userId);
    }

    @Transactional
    public void changePassword(Long userId, PasswordChangeRequestDto requestDto) {

        // 1. 유저 찾기
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundByIdException(userId + " not found"));
        // 2. 현재 비밀번호 검증
        if (!encoder.matches(requestDto.getCurrentPassword(), user.getPassword())) {
            // 💡 [재사용] 기존 예외 사용
            throw new UserWrongPasswordException("현재 비밀번호가 일치하지 않습니다.");
        }
        // 3. (선택) 새 비밀번호가 현재 비밀번호와 같은지 확인
        if (encoder.matches(requestDto.getNewPassword(), user.getPassword())) {
            throw new IllegalArgumentException("새 비밀번호는 현재 비밀번호와 달라야 합니다.");
        }
        // 4. 새 비밀번호 암호화 및 업데이트 (엔티티 헬퍼 메소드 사용)
        user.updatePassword(encoder.encode(requestDto.getNewPassword()));
        // 💡 @Transactional이 종료되면서 변경 감지(Dirty Checking)로 자동 save 됨
    }

}
