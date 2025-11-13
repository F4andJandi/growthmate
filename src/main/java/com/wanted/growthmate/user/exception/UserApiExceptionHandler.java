package com.wanted.growthmate.user.exception;

import com.wanted.growthmate.user.controller.UserController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice(assignableTypes = UserController.class)
public class UserApiExceptionHandler {

    @ExceptionHandler({IllegalArgumentException.class, UserWrongPasswordException.class, UserAlreadyExistsException.class, UserNotFoundByIdException.class})
    public ResponseEntity<String> handleUserExceptions(RuntimeException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    }
}

