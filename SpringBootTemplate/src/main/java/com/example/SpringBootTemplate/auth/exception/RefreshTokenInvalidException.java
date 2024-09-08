package com.example.SpringBootTemplate.auth.exception;

public class RefreshTokenInvalidException extends RuntimeException{

    public RefreshTokenInvalidException() {
        super("refreshToken 값이 유효하지 않습니다.");
    }

    @Override
    public String getMessage() {
        return super.getMessage();
    }
}