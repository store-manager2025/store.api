package com.project.storemanager_api.jwt;

import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;


@Setter
@Configuration
@Slf4j
@ConfigurationProperties(prefix = "jwt")
// application.yml에서 jwt관련 프로퍼티값을 읽어오는 클래스
public class JwtProperties {

    private String secretKey;
    private long accessTokenValidityTime;

    public String getSecretKey() {
        log.info("secretKey: {}", secretKey);
        return secretKey;
    }

    public long getAccessTokenValidityTime() {
        return accessTokenValidityTime;
    }

    public long getRefreshTokenValidityTime() {
        return refreshTokenValidityTime;
    }

    private long refreshTokenValidityTime;
}