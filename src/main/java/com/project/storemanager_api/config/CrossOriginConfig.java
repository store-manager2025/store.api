package com.project.storemanager_api.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

// 전역 크로스오리진 설정
@Configuration
public class CrossOriginConfig implements WebMvcConfigurer {

    private String[] urls = {
            "http://localhost:3000",
    };

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry
                .addMapping("/**") // 어떤 URL 요청에서
                .allowedOrigins(urls) // 어떤 클라이언트를
                .allowedMethods("*") // 어떤 방식에서 (*은 GET,POST등 다 허용)
                .allowedHeaders("*") // 어떤 헤더를 사용할지
                .allowCredentials(true) // 쿠키 전송을 허용할지 (true=허용)
        ;
    }
}
