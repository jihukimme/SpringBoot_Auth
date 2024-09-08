package com.example.SpringBootTemplate.auth.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

@RestControllerAdvice
public class ExceptionAdvisor {

    // MethodArgumentNotValidException, 유효성 처리 발생시 던져지는 Exception을 핸들링
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<String> validationHandler(MethodArgumentNotValidException e){
        List<FieldError> errors = e.getBindingResult().getFieldErrors();

        StringBuilder sb = new StringBuilder();
        for(FieldError error : errors ){
            sb
                    .append(error.getDefaultMessage())
                    .append("\n")
                    .append("입력된 값 : ")
                    .append(error.getRejectedValue())
                    .append("\n");
        }

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(sb.toString());
    }


    // 모든 컨트롤러에대한 예외 핸들러
    @ExceptionHandler({
            // 유저가 존재하지 않을 때
            UserNotFoundException.class,
            // 회원가입시 이미 가입된 이메일일 때
            EmailDuplicationException.class,
            // 비밀번호가 일치하지 않을 때
            PasswordInvalidException.class,
            // 이메일값이 DTO에 존재하지 않을 때 -> Validation으로 막아야할듯
            EmailNullException.class,
            // Refresh Token 만료
            RefreshTokenInvalidException.class
    })
    public ResponseEntity<String> postControllerExceptionHandler(
            Exception e){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    }
}
