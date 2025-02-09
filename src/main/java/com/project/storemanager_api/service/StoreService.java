package com.project.storemanager_api.service;

import com.project.storemanager_api.domain.store.dto.request.DeleteStoreRequestDto;
import com.project.storemanager_api.domain.store.dto.request.ModifyStoreRequestDto;
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
    @Transactional(readOnly = true)
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


    public StoreDetailResponseDto modifyStoreInfo(ModifyStoreRequestDto dto) {
        // 1. 기존 매장 상세 조회 (없으면 예외 발생)
        StoreDetailResponseDto currentStore = storeRepository.findStoreDetailByStoreId(dto.getStoreId());
        if (currentStore == null) {
            throw new StoreException(ErrorCode.STORE_NOT_FOUND, "매장을 찾을 수 없습니다.");
        }
        if (dto.getStoreName() == null || dto.getStoreName().trim().isEmpty() &&
            dto.getStorePlace() == null || dto.getStorePlace().trim().isEmpty() &&
            dto.getPassword() == null || dto.getPassword().trim().isEmpty()
        ) {
            throw new StoreException(ErrorCode.EMPTY_DATA, "매장 이름, 매장 위치, 비밀번호 중 한 값이라도 포함해야 합니다.");
        }

        // 2. storeName 업데이트 처리
        if (dto.getStoreName() == null || dto.getStoreName().trim().isEmpty()) {
            // 클라이언트가 비어있는 값을 보냈으면 기존 값을 그대로 사용
            dto.setStoreName(currentStore.getStoreName());
        }
        // 3. storePlace 업데이트 처리
        if (dto.getStorePlace() == null || dto.getStorePlace().trim().isEmpty()) {
            dto.setStorePlace(currentStore.getStorePlace());
        }
        // 4. password 업데이트 처리
        // 먼저 DB에서 현재 인코딩된 비밀번호를 조회 (매장 상세 조회 결과에 password가 없을 경우 사용)
        String currentEncodedPassword = storeRepository.findPasswordById(dto.getStoreId())
                .orElseThrow(() -> new StoreException(ErrorCode.STORE_NOT_FOUND, "매장 비밀번호를 찾을 수 없습니다."));

        if (dto.getPassword() == null || dto.getPassword().trim().isEmpty()) {
            // 클라이언트가 비어있는 값을 보냈으면 기존 인코딩된 비밀번호를 그대로 사용
            dto.setPassword(currentEncodedPassword);
        } else {
            // 새 비밀번호가 들어왔을 경우
            if (passwordEncoder.matches(dto.getPassword(), currentEncodedPassword)) {
                // 동일하면 기존 인코딩된 비밀번호를 그대로 사용
                dto.setPassword(currentEncodedPassword);
            } else {
                // 새 비밀번호가 다르면 인코딩 후 설정
                dto.setPassword(passwordEncoder.encode(dto.getPassword()));
            }
        }

        // 5. 업데이트 실행
        storeRepository.updateStore(dto);

        // 6. 변경 후 최신 매장 상세 정보 반환
        return storeRepository.findStoreDetailByStoreId(dto.getStoreId());
    }


    public void deleteStore(DeleteStoreRequestDto dto) {

        // 매장이 존재하면
        String originPassword = storeRepository.findPasswordById(dto.getStoreId())
                .orElseThrow(() -> new StoreException(ErrorCode.STORE_NOT_FOUND, ErrorCode.STORE_NOT_FOUND.getMessage()));

        // 만약 비밀번호가 일치하지 않다면
        if (!passwordEncoder.matches(dto.getPassword(), originPassword)) {
            throw new StoreException(ErrorCode.INVALID_PASSWORD, ErrorCode.INVALID_PASSWORD.getMessage());
        }

        storeRepository.deleteStore(dto.getStoreId());

    }
}
