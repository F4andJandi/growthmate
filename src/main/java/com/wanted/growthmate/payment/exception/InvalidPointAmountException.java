package com.wanted.growthmate.payment.exception;

public class InvalidPointAmountException extends RuntimeException {

    public InvalidPointAmountException(int amount) {
        super("금액은 0보다 커야 합니다. 입력값: " + amount);
    }
}
