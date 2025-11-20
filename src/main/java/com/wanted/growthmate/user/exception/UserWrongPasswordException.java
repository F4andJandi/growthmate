package com.wanted.growthmate.user.exception;

public class UserWrongPasswordException extends RuntimeException {
    public UserWrongPasswordException(String message) {
        super(message);
    }
}
