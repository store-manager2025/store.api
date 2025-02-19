package com.project.storemanager_api.domain.interceptor;

import com.project.storemanager_api.jwt.JwtTokenProvider;
import com.project.storemanager_api.repository.StoreRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@Configuration
@Slf4j
@RequiredArgsConstructor
public class StoreAuthenticationInterceptor implements HandlerInterceptor {

    private final StoreRepository storeRepository;
    private final JwtTokenProvider jwtTokenProvider;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        // 1️⃣ HTTP 요청 헤더에서 JWT 토큰 추출
        String token = resolveToken(request);

        // 2️⃣ 토큰 검증
        if (token == null || !jwtTokenProvider.validateToken(token)) {
            log.warn("Invalid or missing JWT token.");
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized");
            return false;
        }

        Long currentLoginUserId = jwtTokenProvider.getCurrentLoginUserId(token);
        List<Long> currentLoginStoreIds = jwtTokenProvider.getCurrentLoginStoreIds(token);
        log.info("currentLoginUserId in interceptor : {} ", currentLoginUserId);
        log.info("currentLoginStoreIds in interceptor : {} ", currentLoginStoreIds);
        return true;
    }

    /**
     * HTTP 요청 헤더에서 JWT 토큰을 추출하는 메서드
     */
    private String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7); // "Bearer " 이후의 토큰 값 반환
        }
        return null;
    }

}
