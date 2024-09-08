package com.example.SpringBootTemplate.auth.service.impl;

import com.example.SpringBootTemplate.auth.config.security.service.JwtProvider;
import com.example.SpringBootTemplate.auth.dao.UserDAO;
import com.example.SpringBootTemplate.auth.dto.AuthResponseDTO;
import com.example.SpringBootTemplate.auth.dto.TokenDTO;
import com.example.SpringBootTemplate.auth.dto.UserDTO;
import com.example.SpringBootTemplate.auth.dto.UserInfoDTO;
import com.example.SpringBootTemplate.auth.entity.UserEntity;
import com.example.SpringBootTemplate.auth.enums.Role;
import com.example.SpringBootTemplate.auth.exception.*;
import com.example.SpringBootTemplate.auth.repository.UserRepository;
import com.example.SpringBootTemplate.auth.service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class AuthServiceImpl implements AuthService {

    private final UserDAO userDAO;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;

    public AuthServiceImpl(
            @Autowired UserDAO userDAO,
            @Autowired PasswordEncoder passwordEncoder,
            @Autowired JwtProvider jwtProvider,
            @Autowired UserRepository userRepository
    ) {
        this.userDAO = userDAO;
        this.passwordEncoder = passwordEncoder;
        this.jwtProvider = jwtProvider;
        this.userRepository = userRepository;
    }


    // 로그인
    @Override
    @Transactional
    public AuthResponseDTO signin(UserDTO userDTO) throws UserNotFoundException, EmailNullException {
        // 이메일 존재 여부 체크
        if (!this.userDAO.existEmail(userDTO.getEmail())) {
            throw new UserNotFoundException();
        }

        try {
            // Optional을 처리하여 UserEntity 가져오기
            Optional<UserEntity> optionalUserEntity = this.userDAO.readUserByEmail(userDTO.getEmail());
            UserEntity userEntity = optionalUserEntity.orElseThrow(UserNotFoundException::new);

            if (!this.passwordEncoder.matches(userDTO.getPassword(), userEntity.getPassword())) {
                throw new PasswordInvalidException();
            }

            return AuthResponseDTO.toAuthResponseDTO(userEntity, jwtProvider.issueToken(userDTO));
        } catch (NullPointerException e) {
            e.printStackTrace();
            throw new EmailNullException();
        }
    }

    // 회원가입
    @Override
    @Transactional
    public AuthResponseDTO signup(UserDTO userDTO) throws EmailDuplicationException, EmailNullException {
        UserEntity userEntity;

        // 이메일 중복 체크
        if (this.userDAO.existEmail(userDTO.getEmail())) {
            throw new EmailDuplicationException();
        }

        try {
            // role이 null일 경우 ROLE_USER로 설정
            if (userDTO.getRole() == null) {
                userDTO.setRole(Role.ROLE_USER);
            }

            userDTO.setPassword(this.passwordEncoder.encode(userDTO.getPassword()));
            userEntity = UserDTO.toEntity(userDTO);

            userEntity = this.userDAO.createUser(userEntity);
            return AuthResponseDTO.toAuthResponseDTO(userEntity, jwtProvider.issueToken(userDTO));
        } catch (NullPointerException | IllegalArgumentException e) {
            e.printStackTrace();
            throw new EmailNullException();
        }
    }

    @Override
    @Transactional
    public void deleteUser(Long id) throws UserNotFoundException{
        Optional<UserEntity> optionalUserEntity = this.userDAO.readUserById(id);
        if (optionalUserEntity.isPresent()) {
            this.userDAO.deleteUser(id);
        } else {
            throw new UserNotFoundException();
        }
    }

    @Override
    public UserInfoDTO getUserInfo(Long id){
        // Optional을 처리하여 UserEntity 가져오기
        Optional<UserEntity> optionalUserEntity = this.userDAO.readUserById(id);
        UserEntity userEntity = optionalUserEntity.orElseThrow(UserNotFoundException::new);

        return UserInfoDTO.toDTO(userEntity);
    }

    @Override
    @Transactional
    public UserInfoDTO modifyUserInfo(Long id, UserInfoDTO userInfoDTO){
        Optional<UserEntity> optionalUserEntity = this.userDAO.readUserById(id);
        UserEntity userEntity = optionalUserEntity.orElseThrow(UserNotFoundException::new);

        userEntity.setEmail(userInfoDTO.getEmail());
        userEntity.setName(userInfoDTO.getName());
        userEntity.setRole(userInfoDTO.getRole());
        userEntity.setPhone(userInfoDTO.getPhone());

        userRepository.save(userEntity);

        return UserInfoDTO.toDTO(userEntity);
    }

    @Override
    public TokenDTO reIssueToken(HttpServletRequest request) throws RefreshTokenInvalidException {
        String token = jwtProvider.resolveToken(request);
        if(jwtProvider.validToken(token)) {
            return jwtProvider.reIssueToken(token);
        }
        else {
            throw new RefreshTokenInvalidException();
        }
    }


}

