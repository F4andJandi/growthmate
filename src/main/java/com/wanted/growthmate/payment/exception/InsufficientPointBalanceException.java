package com.wanted.growthmate.payment.exception;

public class InsufficientPointBalanceException extends RuntimeException {
    public InsufficientPointBalanceException(Long userId, int amount) {
        super("사용자 [" + userId + "] 의 포인트가 부족합니다. 필요한 포인트: " + amount);
    }
}
