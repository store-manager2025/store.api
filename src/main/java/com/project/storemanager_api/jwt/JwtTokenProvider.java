package com.project.storemanager_api.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.crypto.SecretKey;
import java.util.Date;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtTokenProvider {

    private final JwtProperties jwtProperties;

    // 비밀키를 생성
    private SecretKey key;

    @PostConstruct
    public void init() {
        // Base64로 인코딩된 key를 디코딩 후, HMAC-SHA 알고리즘으로 다시 암호화
        log.info("jwtProperties.getSecretKey(): {}", jwtProperties.getSecretKey());
        this.key = Keys.hmacShaKeyFor(
                Decoders.BASE64.decode(jwtProperties.getSecretKey())
        );
    }

    // 토큰 발급 로직
    // 엑세스 토큰 생성 (사용자가 들고다닐 신분증) : 유효기간이 짧다.
    public String createAccessToken(String username) {
        return createToken(username, jwtProperties.getAccessTokenValidityTime());
    }
    // 리프레시 토큰 생성 (서버가 보관할 신분증을 재발급하기 위한 정보) : 유효기간이 비교적 길다.
    public String createRefreshToken(String username) {

        return createToken(username, jwtProperties.getRefreshTokenValidityTime());
    }

    // 공통 토큰 생성 로직
    // 엑세스,리프레시 생성 로직은 똑같고, 시간만 다르다
    private String createToken(String email, long validityTime) {

        // 현재 시간
        Date now = new Date();

        // 만료 시간
        Date validity = new Date(now.getTime() + validityTime);

        return Jwts.builder()
                .setIssuer("store") // 발급자 정보
                .setIssuedAt(now) // 발급 시간
                .setExpiration(validity) // 만료 시간
                .setSubject(email) // 토큰 식별자 (유일한 값)
                .signWith(key) // 서명 포함
                .compact();
    }

    /**
     * 토큰이 유효한지 검증하는 메서드
     * @param token JWT token
     * @return 토큰이 정상이면 true, 만료/위조라면 false
     */
    public boolean validateToken(String token) {
        try {
            parseClaims(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            log.warn("invalid token: {}", e.getMessage(), e);
            return false;
        }
    }


    /**
     * 검증된 토큰에서 사용자이름을 추출하는 메서드
     * @param token - 인증 토큰
     * @return 토큰에서 추출한 사용자 이름
     */
    public String getCurrentLoginUsername(String token) {
        return parseClaims(token).getSubject();
    }

    /**
     * 내부적으로 토큰을 파싱하여 Claims 객체를 반환하는 메서드
     * (인코딩해놓은 문자열의 토큰을 해체하는 메서드)
     * @param token JWT 토큰
     * @return 파싱된 Claims 객체
     * @throws JwtException 토큰이 유효하지 않은 경우 발생
     */
    private Claims parseClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

}

