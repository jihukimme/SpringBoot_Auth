package com.example.SpringBootTemplate.auth.controller;

import com.example.SpringBootTemplate.auth.config.security.service.JwtProvider;
import com.example.SpringBootTemplate.auth.dto.AuthResponseDTO;
import com.example.SpringBootTemplate.auth.dto.TokenDTO;
import com.example.SpringBootTemplate.auth.dto.UserDTO;
import com.example.SpringBootTemplate.auth.dto.UserInfoDTO;
import com.example.SpringBootTemplate.auth.service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import org.springframework.validation.annotation.Validated;

@RestController
@RequestMapping("/auth")
public class AuthController {
    private final AuthService authService;
    private final JwtProvider jwtProvider;

    public AuthController(
            @Autowired AuthService authService,
            @Autowired JwtProvider jwtProvider
    ){
        this.authService = authService;
        this.jwtProvider = jwtProvider;
    }

    // AuthController에 대한 예외 처리는 ExceptionAdvisor에 묶어냈다.

    @PostMapping("/signup")
    public ResponseEntity<AuthResponseDTO> signup(
            @Valid @RequestBody UserDTO userDTO
    ){
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(this.authService.signup(userDTO));
    }

    @PostMapping("/signin")
    public ResponseEntity<AuthResponseDTO> signin(
            @Valid @RequestBody UserDTO userDTO
    ){
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(this.authService.signin(userDTO));
    }

    @PostMapping("/refresh/token")
    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
    public ResponseEntity<TokenDTO> reIssueToken(@Valid HttpServletRequest request) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(this.authService.reIssueToken(request));
    }

    @DeleteMapping("/user")
    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
    public ResponseEntity<Void> deleteUser(HttpServletRequest request) {
        Long userId = getUserId(request);
        this.authService.deleteUser(userId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @GetMapping(value = "/user")
    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
    public ResponseEntity<UserInfoDTO> getUserInfo(HttpServletRequest request){
        Long userId = getUserId(request);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(this.authService.getUserInfo(userId));
    }

    @PutMapping(value = "/user")
    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
    public ResponseEntity<UserInfoDTO> modifyUserInfo(HttpServletRequest request, @RequestBody UserInfoDTO userInfoDTO){
        Long userId = getUserId(request);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(this.authService.modifyUserInfo(userId, userInfoDTO));
    }

    private Long getUserId(HttpServletRequest request){
        String token = jwtProvider.resolveToken(request);
        return Long.parseLong(jwtProvider.getUserId(token));
    }
}
