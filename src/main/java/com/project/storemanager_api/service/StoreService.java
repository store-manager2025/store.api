package com.project.storemanager_api.service;

import com.project.storemanager_api.domain.store.dto.request.SaveStoreRequestDto;
import com.project.storemanager_api.domain.store.dto.request.StoreLoginRequestDto;
import com.project.storemanager_api.domain.store.dto.response.StoreDetailResponseDto;
import com.project.storemanager_api.domain.store.dto.response.StoreResponseDto;
import com.project.storemanager_api.exception.ErrorCode;
import com.project.storemanager_api.exception.StoreException;
import com.project.storemanager_api.repository.StoreRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@Slf4j
@RequiredArgsConstructor
public class StoreService {

    private final StoreRepository storeRepository;
    private final PasswordEncoder passwordEncoder;


    /**
     * 매장 생성
     * @param dto -  storeName, storePlace, password가 들어있는 객체
     * @param userId - Jwt에서 파싱한 userId
     * 입력값 검증 후 이상없으면 password인코딩 후 db 저장
     */
    public void saveStore(SaveStoreRequestDto dto, Long userId) {
        log.info("매장 생성 요청 : {}", userId);
        if (dto.getStoreName().isEmpty() || dto.getStorePlace().isEmpty() || dto.getPassword().isEmpty()) {
            log.info("입력값 비어있음!");
            throw new StoreException(ErrorCode.EMPTY_DATA, "가게 이름, 가게 장소는 필수 입력값입니다.");
        }

        if (dto.getPassword().length() != 4) {
            throw new StoreException(ErrorCode.NOT_VALID_PASSWORD, ErrorCode.NOT_VALID_PASSWORD.getMessage());
        }

        String encodedPassword = passwordEncoder.encode(dto.getPassword());
        dto.setPassword(encodedPassword);

        dto.setUserId(userId);
        storeRepository.saveStore(dto);
    }

    // 한 유저가 생성한 매장목록 전체 조회
    @Transactional(readOnly = true)
    public List<StoreResponseDto> getStoreList(Long userId) {
        log.info("매장 조회 요청 : {}", userId);
        return storeRepository.findStoreList(userId);
    }

    /**
     * 매장 정보 상세 조회
     * @param dto - password, storeId가 들어있는 객체
     * @return - 상세 정보가 들어있는 StoredetailResponseDto
     */
    public StoreDetailResponseDto loginInStore(StoreLoginRequestDto dto) {

        // 1. dto의 store_id로 매장 정보 조회
        String originPassword = storeRepository.findPasswordById(dto.getStoreId())
                .orElseThrow(() -> new StoreException(ErrorCode.STORE_NOT_FOUND, ErrorCode.STORE_NOT_FOUND.getMessage()));
        log.info("originPassword : {}", originPassword);

        // 2. 비밀번호 대조 검사
        if (!passwordEncoder.matches(dto.getPassword(), originPassword)) {
            throw new StoreException(ErrorCode.INVALID_PASSWORD, ErrorCode.INVALID_PASSWORD.getMessage());
        }

        // 3. 일치 하면 정보 넘겨줌
        return storeRepository.findStoreDetailByStoreId(dto.getStoreId());
    }
}
