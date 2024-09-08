package com.example.SpringBootTemplate.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class TokenDTO {
    private String grantType;
    private String accessToken;
    private String refreshToken;
}
