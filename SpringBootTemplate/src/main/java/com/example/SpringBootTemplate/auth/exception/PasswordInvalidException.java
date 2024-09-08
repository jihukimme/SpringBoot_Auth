package com.example.SpringBootTemplate.auth.exception;

public class PasswordInvalidException extends RuntimeException{
    public PasswordInvalidException() {
        super("password 값이 유효하지 않습니다.");
    }
    @Override
    public String getMessage() {
        return super.getMessage();
    }
}