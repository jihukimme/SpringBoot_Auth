package com.example.SpringBootTemplate.auth.exception;

public class EmailDuplicationException extends RuntimeException{

    public EmailDuplicationException() {
        super("해당 email 값이 이미 존재합니다.");
    }

    @Override
    public String getMessage() {
        return super.getMessage();
    }
}
