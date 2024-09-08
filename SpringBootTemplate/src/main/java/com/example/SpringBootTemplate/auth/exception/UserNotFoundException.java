package com.example.SpringBootTemplate.auth.exception;

public class UserNotFoundException extends RuntimeException{

    public UserNotFoundException() {
        super("user를 찾을 수 없습니다.");
    }

    @Override
    public String getMessage() {
        return super.getMessage();
    }
}