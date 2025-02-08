package com.project.storemanager_api.service;

import com.project.storemanager_api.domain.store.dto.request.SaveStoreRequestDto;
import com.project.storemanager_api.exception.ErrorCode;
import com.project.storemanager_api.exception.StoreException;
import com.project.storemanager_api.repository.StoreRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@Slf4j
@RequiredArgsConstructor
public class StoreService {

    private final StoreRepository storeRepository;


    /**
     * 매장 생성
     * @param dto -  storeName, storePlace가 들어있는 객체
     * @param userId - Jwt에서 파싱한 userId
     */
    public void saveStore(SaveStoreRequestDto dto, Long userId) {

        if (dto.getStoreName().isEmpty() || dto.getStorePlace().isEmpty()) {
            log.info("입력값 비어있음!");
            throw new StoreException(ErrorCode.EMPTY_DATA, "가게 이름, 가게 장소는 필수 입력값입니다.");
        }
        dto.setUserId(userId);
        storeRepository.saveStore(dto);
    }
}
