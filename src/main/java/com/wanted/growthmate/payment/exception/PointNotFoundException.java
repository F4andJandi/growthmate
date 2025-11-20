package com.wanted.growthmate.payment.exception;

public class PointNotFoundException extends RuntimeException {
    public PointNotFoundException(Long userId) {
        super("사용자 ID " + userId + "의 포인트 원장이 존재하지 않습니다.");
    }
}
