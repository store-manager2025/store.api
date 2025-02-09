package com.project.storemanager_api.service;

import com.project.storemanager_api.domain.store.dto.request.SaveStoreRequestDto;
import com.project.storemanager_api.exception.ErrorCode;
import com.project.storemanager_api.exception.StoreException;
import com.project.storemanager_api.repository.StoreRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

        if (dto.getStoreName().isEmpty() || dto.getStorePlace().isEmpty() || dto.getPassword().isEmpty()) {
            log.info("입력값 비어있음!");
            throw new StoreException(ErrorCode.EMPTY_DATA, "가게 이름, 가게 장소는 필수 입력값입니다.");
        }

        if (dto.getPassword().length() != 4) {
            throw new StoreException(ErrorCode.PASSWORD_TOO_SHORT, ErrorCode.PASSWORD_TOO_SHORT.getMessage());
        }

        String encodedPassword = passwordEncoder.encode(dto.getPassword());
        dto.setPassword(encodedPassword);

        dto.setUserId(userId);
        storeRepository.saveStore(dto);
    }
}
