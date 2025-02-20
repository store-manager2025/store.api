package com.project.storemanager_api.aspect;

import com.project.storemanager_api.annotation.StoreAuthCheck;
import com.project.storemanager_api.exception.ErrorCode;
import com.project.storemanager_api.exception.StoreException;
import com.project.storemanager_api.domain.user.dto.response.CustomUserPrincipal;
import com.project.storemanager_api.validator.StoreValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

@Component
@Aspect
@RequiredArgsConstructor
@Slf4j
public class StoreAuthAspect {

    private final StoreValidator storeValidator;

    @Before("@annotation(storeAuthCheck)")
    public void checkStoreAuth(JoinPoint joinPoint, StoreAuthCheck storeAuthCheck) {
        log.info("joinPoint: {}", joinPoint);
        Object[] args = joinPoint.getArgs();
        // 첫 번째 인자는 CustomUserPrincipal라고 가정
        CustomUserPrincipal userInfo = (CustomUserPrincipal) args[0];
        // 두 번째 인자는 storeId를 포함하는 DTO라고 가정 (예: ModifyStoreRequestDto)
        Object dto = args[1];
        Long storeId;
        try {
            // DTO에 getStoreId() 메서드가 있다고 가정
            storeId = (Long) dto.getClass().getMethod("getStoreId").invoke(dto);
        } catch (Exception e) {
            throw new StoreException(ErrorCode.EMPTY_DATA, "Store Id가 없습니다.");
        }
        log.info("StoreAuthAspect: userInfo = {}, storeId = {}", userInfo, storeId);
        // 검증 로직 호출 (검증 실패 시 예외 발생)
        storeValidator.checkStoreAuth(userInfo, storeId);
    }
}