package com.changseon.StudyWithChang.global.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())  // CSRF 보호 비활성화
                .authorizeHttpRequests(authorize ->
                        authorize
                                .requestMatchers("/api/member/signup").permitAll()  // 회원가입 API는 인증 없이 허용
                                .anyRequest().authenticated()  // 나머지 요청은 인증 필요
                );


        return http.build();
    }
}
