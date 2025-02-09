package com.project.storemanager_api.validator;

import com.project.storemanager_api.domain.store.dto.request.ModifyStoreRequestDto;
import com.project.storemanager_api.domain.store.dto.request.SaveStoreRequestDto;
import com.project.storemanager_api.domain.store.dto.request.StoreLoginRequestDto;
import com.project.storemanager_api.domain.store.dto.response.StoreDetailResponseDto;
import com.project.storemanager_api.exception.ErrorCode;
import com.project.storemanager_api.exception.StoreException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class StoreValidator {

    private final PasswordEncoder passwordEncoder;

    public StoreValidator(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * 매장 생성 시 입력값 검증
     */
    public void validateSaveStoreInput(SaveStoreRequestDto dto) {
        if (dto.getStoreName() == null || dto.getStoreName().trim().isEmpty() ||
                dto.getStorePlace() == null || dto.getStorePlace().trim().isEmpty() ||
                dto.getPassword() == null || dto.getPassword().trim().isEmpty()) {
            throw new StoreException(ErrorCode.EMPTY_DATA, "가게 이름, 가게 장소, 비밀번호는 필수 입력값입니다.");
        }
        if (dto.getPassword().length() != 4) {
            throw new StoreException(ErrorCode.NOT_VALID_PASSWORD, ErrorCode.NOT_VALID_PASSWORD.getMessage());
        }
    }

    /**
     * 매장 로그인 시 입력값 검증
     */
    public void validateStoreLoginInput(StoreLoginRequestDto dto) {
        if (dto.getStoreId() == null ||
                dto.getPassword() == null || dto.getPassword().trim().isEmpty()) {
            throw new StoreException(ErrorCode.EMPTY_DATA, "매장 ID와 비밀번호는 필수 입력값입니다.");
        }
    }

    /**
     * 비밀번호 일치 여부 검증 (로그인용)
     */
    public void validatePassword(String rawPassword, String encodedPassword) {
        if (!passwordEncoder.matches(rawPassword, encodedPassword)) {
            throw new StoreException(ErrorCode.INVALID_PASSWORD, "비밀번호가 일치하지 않습니다.");
        }
    }

    /**
     * 매장 수정 시 입력값 검증 및 기본값 적용
     * 클라이언트에서 비어있는 필드가 있으면, 기존 값을 그대로 사용하고,
     * 비밀번호가 새로 입력된 경우에는 기존과 동일한지 비교 후, 다르면 인코딩하여 설정
     *
     * @param dto                   수정 요청 DTO
     * @param currentStore          현재 매장 상세 정보 (기존 값)
     * @param currentEncodedPassword DB에 저장된 인코딩된 비밀번호
     */
    public void prepareModifyStoreInput(ModifyStoreRequestDto dto,
                                        StoreDetailResponseDto currentStore,
                                        String currentEncodedPassword) {
        // storeName: 값이 없으면 기존 값 사용
        if (dto.getStoreName() == null || dto.getStoreName().trim().isEmpty()) {
            dto.setStoreName(currentStore.getStoreName());
        }
        // storePlace: 값이 없으면 기존 값 사용
        if (dto.getStorePlace() == null || dto.getStorePlace().trim().isEmpty()) {
            dto.setStorePlace(currentStore.getStorePlace());
        }
        // password: 값이 없으면 기존 비밀번호 사용, 있으면 비교 후 처리
        if (dto.getPassword() == null || dto.getPassword().trim().isEmpty()) {
            dto.setPassword(currentEncodedPassword);
        } else {
            if (passwordEncoder.matches(dto.getPassword(), currentEncodedPassword)) {
                // 입력된 비밀번호가 기존과 동일하면 그대로 사용
                dto.setPassword(currentEncodedPassword);
            } else {
                // 새 비밀번호가 다르면 인코딩 후 설정
                dto.setPassword(passwordEncoder.encode(dto.getPassword()));
            }
        }
    }
}