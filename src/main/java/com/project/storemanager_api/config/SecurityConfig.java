package com.project.storemanager_api.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity // 커스텀 시큐리티 설정파일이라는 의미
@RequiredArgsConstructor
public class SecurityConfig {


    // 시큐리티 필터체인 빈을 등록
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        // 커스텀 보안 설정
        http
                .csrf(csrf -> csrf.disable()) // 스프링 시큐리티 기본 보안창 비활성화
                .cors(cors -> cors.configure(http))

        ;

        return http.build();
    }

}

