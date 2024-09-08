package com.example.SpringBootTemplate.auth.exception;

public class EmailNullException extends RuntimeException{
    public EmailNullException() {
        super("email 값이 유효하지 않습니다.");
    }

    @Override
    public String getMessage() {
        return super.getMessage();
    }
}