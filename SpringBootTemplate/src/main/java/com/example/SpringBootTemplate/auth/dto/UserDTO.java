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
public class UserDTO {
    private Long id;
    private String email;
    private String password;
    private String name;
    private Role role; // Role enum 사용
    private String phone;

    // DTO를 Entity로 변환하는 메소드
    public static UserEntity toEntity(UserDTO userDTO){
        return UserEntity.builder()
                .id(userDTO.getId())
                .email(userDTO.getEmail())
                .password(userDTO.getPassword())
                .name(userDTO.getName())
                .role(userDTO.getRole())  // 수정된 부분: Role을 직접 할당
                .phone(userDTO.getPhone())
                .build();
    }

    // Entity를 DTO로 변환하는 메소드
    public static UserDTO toDTO(UserEntity userEntity) {
        return UserDTO.builder()
                .id(userEntity.getId())
                .email(userEntity.getEmail())
                .password(userEntity.getPassword())
                .name(userEntity.getName())
                .role(userEntity.getRole())  // 수정된 부분: Role을 직접 할당
                .phone(userEntity.getPhone())
                .build();
    }
}
