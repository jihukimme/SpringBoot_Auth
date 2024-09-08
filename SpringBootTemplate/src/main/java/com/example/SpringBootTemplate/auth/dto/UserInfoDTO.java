package com.example.SpringBootTemplate.auth.dto;

import com.example.SpringBootTemplate.auth.entity.UserEntity;
import com.example.SpringBootTemplate.auth.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserInfoDTO {
    private String email;
    private String name;
    private Role role;
    private String phone;

    // Entity를 DTO로 변환하는 메소드
    public static UserInfoDTO toDTO(UserEntity userEntity) {
        return UserInfoDTO.builder()
                .email(userEntity.getEmail())
                .name(userEntity.getName())
                .role(userEntity.getRole())  // 수정된 부분: Role을 직접 할당
                .phone(userEntity.getPhone())
                .build();
    }
}
