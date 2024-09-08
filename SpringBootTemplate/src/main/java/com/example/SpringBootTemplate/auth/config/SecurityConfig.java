package com.example.SpringBootTemplate.auth.config;

import com.example.SpringBootTemplate.auth.config.security.filter.JwtAuthenticationFilter;
import com.example.SpringBootTemplate.auth.config.security.service.JwtProvider;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;


@Configuration
@EnableWebSecurity
@EnableMethodSecurity(securedEnabled = true)
@RequiredArgsConstructor
public class SecurityConfig {
    private final JwtProvider jwtProvider;

    //개발용
    private static final String[] AUTH_WHITELIST = {
        "/auth/signup", //회원가입
        "/auth/signin", //로그인
    };

    //배포용
    //private static final String[] AUTH_WHITELIST = {"/auth/**",};

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable()) // CSRF 보호 비활성화
                .cors(cors -> cors.configurationSource(corsConfigurationSource())) // CORS 설정 적용
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // 세션 관리 정책: STATELESS
                .formLogin((form)->form.disable())
                .httpBasic(AbstractHttpConfigurer::disable)//FormLogin, BasicHttp 비활성화
                .authorizeHttpRequests(auth -> auth
                        //.requestMatchers("auth/**").permitAll() //개발용 전체 허용
                        .requestMatchers(AUTH_WHITELIST).permitAll()
                        .anyRequest().authenticated() // 그 외 모든 요청은 인증 요구
                )
                .addFilterBefore(new JwtAuthenticationFilter(jwtProvider), UsernamePasswordAuthenticationFilter.class); // JWT 인증 필터 추가
        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();

        //배포용
        //configuration.addAllowedOriginPattern("http://*.example.com"); // example.com의 모든 서브도메인을 허용
        //configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        //configuration.setAllowedHeaders(Arrays.asList("Authorization", "Cache-Control", "Content-Type"));
        //configuration.setAllowCredentials(true);

        //개발용
        configuration.addAllowedOriginPattern("*");
        configuration.addAllowedMethod("*");
        configuration.addAllowedHeader("*");
        configuration.setAllowCredentials(false);


        configuration.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

}


