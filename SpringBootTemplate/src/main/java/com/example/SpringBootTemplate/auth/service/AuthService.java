package com.example.SpringBootTemplate.auth.service;

import com.example.SpringBootTemplate.auth.dto.AuthResponseDTO;
import com.example.SpringBootTemplate.auth.dto.TokenDTO;
import com.example.SpringBootTemplate.auth.dto.UserDTO;
import com.example.SpringBootTemplate.auth.dto.UserInfoDTO;
import jakarta.servlet.http.HttpServletRequest;

public interface AuthService {
    public AuthResponseDTO signin(UserDTO userDTO);
    public AuthResponseDTO signup(UserDTO userDTO);
    public void deleteUser(Long id);
    UserInfoDTO getUserInfo(Long id); // getUserInfo 추가
    UserInfoDTO modifyUserInfo(Long id, UserInfoDTO userInfoDTO); // modifyUserInfo 추가
    public TokenDTO reIssueToken(HttpServletRequest request);
}
