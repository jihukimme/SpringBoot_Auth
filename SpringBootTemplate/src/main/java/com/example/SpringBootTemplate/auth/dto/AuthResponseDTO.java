package com.example.SpringBootTemplate.auth.dto;

import com.example.SpringBootTemplate.auth.entity.UserEntity;
import com.example.SpringBootTemplate.auth.enums.Role;
import lombok.*;

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class AuthResponseDTO {
    private String email;
    private String name;
    private Role role;
    private TokenDTO token;

    // Entity를 DTO로 변환하는 메소드
    public static AuthResponseDTO toAuthResponseDTO(UserEntity userEntity, TokenDTO tokenDTO) {
        return AuthResponseDTO.builder()
                .email(userEntity.getEmail())
                .name(userEntity.getName())
                .role(userEntity.getRole())  // 수정된 부분: Role을 직접 할당
                .token(tokenDTO)
                .build();
    }}
