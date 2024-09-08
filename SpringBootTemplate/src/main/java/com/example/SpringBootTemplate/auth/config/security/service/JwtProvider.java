package com.example.SpringBootTemplate.auth.config.security.service;

import com.example.SpringBootTemplate.auth.dto.AuthResponseDTO;
import com.example.SpringBootTemplate.auth.dto.TokenDTO;
import com.example.SpringBootTemplate.auth.dto.UserDTO;
import com.example.SpringBootTemplate.auth.enums.Role;
import io.jsonwebtoken.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.util.Base64;
import java.util.Date;

@Component
public class JwtProvider {

    private static final Logger logger = LoggerFactory.getLogger(JwtProvider.class);

    private final Key secretKey;
    private final long accessTokenExpTime;
    private final long refreshTokenExpTime;
    private final UserDetailsService userDetailsService;

    public JwtProvider(@Value("${jwt.secret}") String secretKey,
                       @Value("${jwt.accessTokenExpTime}") long accessTokenExpTime,
                       @Value("${jwt.refreshTokenExpTime}") long refreshTokenExpTime,
                       UserDetailsService userDetailsService
    ){
        byte[] decodedKey = Base64.getDecoder().decode(secretKey);
        this.secretKey = new SecretKeySpec(decodedKey, SignatureAlgorithm.HS256.getJcaName());
        this.accessTokenExpTime=accessTokenExpTime;
        this.refreshTokenExpTime=refreshTokenExpTime;
        this.userDetailsService=userDetailsService;
    }

    public TokenDTO issueToken(UserDTO userDTO){
        Claims claims = Jwts.claims().setSubject(userDTO.getEmail());
        claims.put("role", userDTO.getRole());
        String accessToken = createToken(claims, accessTokenExpTime);
        String refreshToken = createToken(claims, refreshTokenExpTime);

        return TokenDTO.builder()
                .grantType("Bearer")
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    public TokenDTO reIssueToken(String token){
        Jws<Claims> claimsJws = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token);
        Claims claims = claimsJws.getBody();

        String accessToken = createToken(claims, accessTokenExpTime);
        String refreshToken = createToken(claims, refreshTokenExpTime);

        return TokenDTO.builder()
                .grantType("Bearer")
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }


    private String createToken(Claims claims, long expTime) {
        Date now = new Date(); // 매번 최신 시간을 사용

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + expTime))
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }


    @Transactional
    public Authentication getAuthentication(String token) {
        logger.info("토큰 인증 시작");
        UserDetails userDetails = userDetailsService.loadUserByUsername(this.getUserId(token));
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }


    public String getUserId(String token){
        String userId = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody().getSubject();
        return userId;
    }


    public String resolveToken(HttpServletRequest request) {
        try {
            return request.getHeader("Authorization").replace("Bearer ", "");
        }catch (NullPointerException e){
            logger.info("토큰이 없습니다.");
            return null;
        }

    }



    public boolean validToken(String token){
        try {
            logger.info("토큰 유효성 검사");
            Jws<Claims> claims = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token);
            return !claims.getBody().getExpiration().before(new Date());
        }catch (Exception e){
            logger.info("토큰 유효성 실패");
            return false;
        }
    }
}
