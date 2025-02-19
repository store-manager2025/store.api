package com.project.storemanager_api.config;

import com.project.storemanager_api.domain.interceptor.StoreAuthenticationInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@RequiredArgsConstructor
public class InterceptorConfig implements WebMvcConfigurer {

    private final StoreAuthenticationInterceptor storeAuthenticationInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {

        // Store API 인가 처리 인터셉터
        registry
                .addInterceptor(storeAuthenticationInterceptor)
                .addPathPatterns("/api/stores/**") // 차단
                .excludePathPatterns("/api/stores/replies/*/page/*");
    }

}
