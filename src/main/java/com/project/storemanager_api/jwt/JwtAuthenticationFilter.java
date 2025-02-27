package com.project.storemanager_api.jwt;

import com.project.storemanager_api.domain.user.dto.response.CustomUserPrincipal;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

// 토큰 검증만 수행 - 토큰이 없거나 위조되거나 만료되었으면 요청을 돌려보냄
@Component
@Slf4j
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenProvider tokenProvider;


    // 실제 필터링 로직을 수행하는 메서드
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        // 사용자가 전달한 토큰을 가져와야 함.
        String token = resolveToken(request);

        // 토큰이 유효하면 SecurityContext에 사용자 정보 저장
        if (StringUtils.hasText(token) && tokenProvider.validateToken(token)) {
            // 유저 ID 및 소유 매장 목록 추출
            Long userId = tokenProvider.getCurrentLoginUserId(token);
            List<Long> currentLoginStoreIds = tokenProvider.getCurrentLoginStoreIds(token);
            String role = "ROLE_" + tokenProvider.getCurrentUserRole(token);
            List<SimpleGrantedAuthority> authorities = Collections.singletonList(new SimpleGrantedAuthority(role));

            //  CustomUserPrincipal을 이용해 SecurityContext에 저장
            CustomUserPrincipal userPrincipal = new CustomUserPrincipal(userId, currentLoginStoreIds, role);
            Authentication authentication = new UsernamePasswordAuthenticationToken(userPrincipal, null, authorities);
            SecurityContextHolder.getContext().setAuthentication(authentication);

            log.info("Authentication success - User ID: {}, Stores: {}, role: {}", userId, currentLoginStoreIds, role);
        }

        // 다음 필터로 요청 전달
        filterChain.doFilter(request, response);
    }



    /**
     * 요청에서 JWT 토큰을 추출하는 메서드
     */
    private String resolveToken(HttpServletRequest request) {
        // API 요청일 경우 헤더에서 추출
        String bearerToken = request.getHeader("Authorization");
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }

        // 쿠키에서 추출 (클라이언트가 브라우저라면 쿠키 기반 가능)
        if (request.getCookies() != null) {
            return Arrays.stream(request.getCookies())
                    .filter(c -> "accessToken".equals(c.getName()))
                    .findFirst()
                    .map(Cookie::getValue)
                    .orElse(null);
        }

        return null;
    }
}