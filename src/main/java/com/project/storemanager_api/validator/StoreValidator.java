package com.project.storemanager_api.validator;

import com.project.storemanager_api.domain.store.dto.request.ModifyStoreRequestDto;
import com.project.storemanager_api.domain.store.dto.request.SaveStoreRequestDto;
import com.project.storemanager_api.domain.store.dto.request.StoreLoginRequestDto;
import com.project.storemanager_api.domain.store.dto.response.StoreDetailResponseDto;
import com.project.storemanager_api.domain.user.dto.response.CustomUserPrincipal;
import com.project.storemanager_api.exception.ErrorCode;
import com.project.storemanager_api.exception.StoreException;
import com.project.storemanager_api.exception.UserException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.regex.Pattern;

@Component
@Slf4j
public class StoreValidator {

    private final PasswordEncoder passwordEncoder;
    private static final Pattern PHONE_PATTERN = Pattern.compile("^010-\\d{4}-\\d{4}$");


    public StoreValidator(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * 매장 생성 시 입력값 검증
     */
    public void validateSaveStoreInput(SaveStoreRequestDto dto) {
        String phoneNumber = dto.getPhoneNumber();
        if (dto.getStoreName() == null || dto.getStoreName().trim().isEmpty() ||
                dto.getStorePlace() == null || dto.getStorePlace().trim().isEmpty() ||
                dto.getPassword() == null || dto.getPassword().trim().isEmpty() ||
                phoneNumber == null || phoneNumber.trim().isEmpty()
        ) {
            throw new StoreException(ErrorCode.EMPTY_DATA, "가게 이름, 가게 장소, 비밀번호, 연락처는 필수 입력값입니다.");
        }
        if (dto.getPassword().length() != 4) {
            throw new StoreException(ErrorCode.NOT_VALID_PASSWORD, ErrorCode.NOT_VALID_PASSWORD.getMessage());
        }
        if (phoneNumber.length() != 11) {
            throw new StoreException(ErrorCode.NOT_CORRECT_PHONE_NUMBER, ErrorCode.NOT_CORRECT_PHONE_NUMBER.getMessage());
        }

        // 전화번호가 "010-1234-1234" 형식이 아니라면(예: "01012341234"인 경우)
        if (!PHONE_PATTERN.matcher(phoneNumber).matches()) {
            // 11자리 숫자라고 가정하고 포맷팅
            String formattedPhone = getFormattedPhone(phoneNumber);
            dto.setPhoneNumber(formattedPhone);
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
        // 모든 입력값이 비어있는지 검사
        if (isBlank(dto.getStoreName()) && isBlank(dto.getStorePlace())
                && isBlank(dto.getPassword()) && isBlank(dto.getPhoneNumber())) {
            throw new StoreException(ErrorCode.EMPTY_DATA, "한 개 이상의 값을 입력해야 합니다.");
        }

        // storeName, storePlace, phoneNumber는 값이 없으면 기존 값을 적용
        dto.setStoreName(defaultIfBlank(dto.getStoreName(), currentStore.getStoreName()));
        dto.setStorePlace(defaultIfBlank(dto.getStorePlace(), currentStore.getStorePlace()));

        String phoneNumber = dto.getPhoneNumber();
        if (isBlank(phoneNumber)) {
            dto.setPhoneNumber(currentStore.getPhoneNumber());
        } else {
            // 하이픈이 없는 11자리 숫자면 포맷팅하고, 그렇지 않으면 그대로 사용
            if (phoneNumber.length() != 11) {
                throw new StoreException(ErrorCode.NOT_CORRECT_PHONE_NUMBER, ErrorCode.NOT_CORRECT_PHONE_NUMBER.getMessage());
            }
            if (!PHONE_PATTERN.matcher(phoneNumber).matches()) {
                dto.setPhoneNumber(getFormattedPhone(phoneNumber));
            } else {
                dto.setPhoneNumber(phoneNumber);
            }
        }

        // password 처리: 비어있으면 기존 인코딩된 비밀번호 사용, 입력값이 있으면 비교 후 처리
        String password = dto.getPassword();
        if (isBlank(password)) {
            dto.setPassword(currentEncodedPassword);
        } else {
            if (passwordEncoder.matches(password, currentEncodedPassword)) {
                dto.setPassword(currentEncodedPassword);
            } else {
                dto.setPassword(passwordEncoder.encode(password));
            }
        }
    }

    // 입력값이 null이거나 공백이면 true
    private boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }

    // 입력값이 공백이면 기존값 반환, 그렇지 않으면 입력값 반환
    private String defaultIfBlank(String newValue, String existingValue) {
        return isBlank(newValue) ? existingValue : newValue;
    }


    /**
     *
     * @param userInfo - 입력을 보낸 사용자의 userId, storeIdList가 들어있는 객체
     * @param storeId - 요청을 원하는 storeId
     */
    public void checkStoreAuth(CustomUserPrincipal userInfo, Long storeId) {
        // 대조검사 실행
        if (userInfo.getStoreIds().stream().noneMatch(storeId::equals)) {
            log.info("권한 없음!");
            throw new UserException(ErrorCode.UNAUTHORIZED, ErrorCode.UNAUTHORIZED.getMessage());
        }
        log.info("권한 유효.");
    }

    private static String getFormattedPhone(String phoneNumber) {
        return phoneNumber.substring(0, 3) + "-"
                + phoneNumber.substring(3, 7) + "-"
                + phoneNumber.substring(7, 11);
    }
}