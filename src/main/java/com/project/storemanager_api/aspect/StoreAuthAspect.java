package com.project.storemanager_api.aspect;

import com.project.storemanager_api.annotation.StoreAuthCheck;
import com.project.storemanager_api.domain.user.dto.response.CustomUserPrincipal;
import com.project.storemanager_api.exception.ErrorCode;
import com.project.storemanager_api.exception.StoreException;
import com.project.storemanager_api.validator.StoreValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.HandlerMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@Component
@Aspect
@RequiredArgsConstructor
@Slf4j
public class StoreAuthAspect {

    private final StoreValidator storeValidator;

    @Before("@annotation(storeAuthCheck)")
    public void checkStoreAuth(JoinPoint joinPoint, StoreAuthCheck storeAuthCheck) {
        log.info("joinPoint: {}", joinPoint);

        // SecurityContext에서 현재 사용자 정보 가져오기
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !(authentication.getPrincipal() instanceof CustomUserPrincipal)) {
            throw new StoreException(ErrorCode.EMPTY_DATA, "User 정보가 없습니다.");
        }

        CustomUserPrincipal userInfo = (CustomUserPrincipal) authentication.getPrincipal();

        // joinPoint에서 storeId 가져오기
        Object[] args = joinPoint.getArgs();
        Object dto = args.length > 0 ? args[0] : null;
        Long storeId = getStoreIdInDto(dto);

        if (storeId == null) {
            throw new StoreException(ErrorCode.EMPTY_DATA, "Store Id가 없습니다.");
        }

        log.info("StoreAuthAspect: userInfo = {}, storeId = {}", userInfo, storeId);
        storeValidator.checkStoreAuth(userInfo, storeId);
    }

    private static Long getStoreIdInDto(Object dto) {
        if (dto == null) {
            return null;
        }

        // dto가 Long 타입이면 그대로 반환
        if (dto instanceof Long) {
            return (Long) dto;
        }

        Long storeId = null;
        try {
            // DTO 객체에 getStoreId() 메서드가 존재하면 호출
            storeId = (Long) dto.getClass().getMethod("getStoreId").invoke(dto);
        } catch (Exception e) {
            log.warn("DTO에서 storeId 추출 실패: {}", e.getMessage());
        }

        // DTO에서 storeId를 추출하지 못했으면, 요청 객체에서 추출 시도
        if (storeId == null) {
            ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            if (attr != null) {
                return getStoreIdInPathVariable(attr);
            }
        }
        return storeId;
    }

    private static Long getStoreIdInPathVariable(ServletRequestAttributes attr) {

        HttpServletRequest request = attr.getRequest();
        Long storeId = null;
        // PathVariable 추출
        @SuppressWarnings("unchecked")
        Map<String, String> pathVars = (Map<String, String>) request.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE);
        String id = "storeId";
        if (pathVars != null && pathVars.containsKey(id)) {
            try {
                storeId = Long.valueOf(pathVars.get(id));
                log.info("PathVariable에서 storeId 추출: {}", storeId);
            } catch (NumberFormatException e) {
                log.warn("PathVariable storeId 변환 실패: {}", e.getMessage());
            }
        }

        // Query Parameter에서 추출
        if (storeId == null) {
            String storeIdStr = request.getParameter(id);
            if (storeIdStr != null && !storeIdStr.isEmpty()) {
                try {
                    storeId = Long.valueOf(storeIdStr);
                    log.info("Query Parameter에서 storeId 추출: {}", storeId);
                } catch (NumberFormatException e) {
                    log.warn("Query Parameter storeId 변환 실패: {}", e.getMessage());
                }
            }
        }
        return storeId;
    }
}