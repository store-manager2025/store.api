package com.project.storemanager_api.service;

import com.project.storemanager_api.domain.category.dto.request.SaveCategoryDto;
import com.project.storemanager_api.domain.store.dto.request.DeleteStoreRequestDto;
import com.project.storemanager_api.domain.store.dto.request.ModifyStoreRequestDto;
import com.project.storemanager_api.domain.store.dto.request.SaveStoreRequestDto;
import com.project.storemanager_api.domain.store.dto.request.StoreLoginRequestDto;
import com.project.storemanager_api.domain.store.dto.response.StoreDetailResponseDto;
import com.project.storemanager_api.domain.store.dto.response.StoreResponseDto;
import com.project.storemanager_api.exception.ErrorCode;
import com.project.storemanager_api.exception.StoreException;
import com.project.storemanager_api.exception.UserException;
import com.project.storemanager_api.jwt.JwtTokenProvider;
import com.project.storemanager_api.repository.StoreRepository;
import com.project.storemanager_api.validator.StoreValidator;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Service
@Transactional
@Slf4j
@RequiredArgsConstructor
public class StoreService {

    private final StoreRepository storeRepository;
    private final PasswordEncoder passwordEncoder;
    private final StoreValidator storeValidator;
    private final CategoryService categoryService;
    private final JwtTokenProvider jwtTokenProvider;

    /**
     * 매장 생성
     * 입력값 검증 후, 비밀번호 인코딩 및 DB 저장
     */
    public void saveStore(SaveStoreRequestDto dto, Long userId) {
        log.info("매장 생성 요청 : userId={}", userId);
        // 입력값 검증
        storeValidator.validateSaveStoreInput(dto);
        // 비밀번호 인코딩
        String encodedPassword = passwordEncoder.encode(dto.getPassword());
        dto.setPassword(encodedPassword);
        // JWT 등에서 추출한 userId 설정
        dto.setUserId(userId);


        // 매장 생성
        storeRepository.saveStore(dto);
        Long generatedStoreId = dto.getStoreId();
        log.info("생성된 storeId: {}", generatedStoreId);

        // 기본 카테고리 하나 생성
        categoryService.saveCategory(
                SaveCategoryDto.builder()
                        .storeId(generatedStoreId)
                        .categoryName("category_1")
                        .build()
        );
    }

    /**
     * 한 유저가 생성한 매장 목록 조회
     */
    @Transactional(readOnly = true)
    public List<StoreResponseDto> getStoreList(Long userId) {
        log.info("매장 목록 조회 요청 : userId={}", userId);
        return storeRepository.findStoreList(userId);
    }

    /**
     * 매장 로그인
     * 입력된 비밀번호와 DB에 저장된 인코딩된 비밀번호를 비교하여 매장 상세 정보 반환
     */
    @Transactional(readOnly = true)
    public StoreDetailResponseDto loginInStore(StoreLoginRequestDto dto) {
        // 로그인 입력 검증
        storeValidator.validateStoreLoginInput(dto);
        // DB에서 인코딩된 비밀번호 조회 (없으면 예외 발생)
        String originPassword = storeRepository.findPasswordById(dto.getStoreId())
                .orElseThrow(() -> new StoreException(ErrorCode.STORE_NOT_FOUND, "매장을 찾을 수 없습니다."));
        log.info("originPassword: {}", originPassword);
        // 비밀번호 비교 검증
        storeValidator.validatePassword(dto.getPassword(), originPassword);

        // 매장 정보 상세 조회
        return storeRepository.findStoreDetailByStoreId(dto.getStoreId());
    }

    /**
     * 매장 정보 수정 시, JWT 토큰의 Claims에 포함된 소유 매장(storeIds) 리스트에
     * 요청된 매장(storeId)가 있는지 확인한 후 수정 처리.
     * .
     * 입력된 값이 비어있는 경우, 기존 값을 그대로 사용하고 비밀번호는 인코딩 후 업데이트 처리
     * @param dto   수정 요청 DTO (storeId, storeName, storePlace, password 등)
     * @param request JWT 토큰 (Authorization 헤더에서 추출)
     */
    public void modifyStoreInfo(ModifyStoreRequestDto dto, HttpServletRequest request) {

        validateToken(dto.getStoreId(), request);

        // 기존 매장 상세 정보 조회
        StoreDetailResponseDto currentStore = storeRepository.findStoreDetailByStoreId(dto.getStoreId());
        if (currentStore == null) {
            throw new StoreException(ErrorCode.STORE_NOT_FOUND, "매장을 찾을 수 없습니다.");
        }
        // DB에서 현재 인코딩된 비밀번호 조회
        String currentEncodedPassword = storeRepository.findPasswordById(dto.getStoreId())
                .orElseThrow(() -> new StoreException(ErrorCode.STORE_NOT_FOUND, "매장 비밀번호를 찾을 수 없습니다."));
        // 수정 입력값 준비 (비어있으면 기존 값 적용, 비밀번호는 인코딩 처리)
        storeValidator.prepareModifyStoreInput(dto, currentStore, currentEncodedPassword);
        // 업데이트 실행
        storeRepository.updateStore(dto);
        // 업데이트 후 최신 매장 상세 정보 반환
    }

    /**
     * 매장 삭제
     * 입력된 비밀번호와 DB에 저장된 비밀번호가 일치하는지 확인 후 삭제
     */
    public void deleteStore(DeleteStoreRequestDto dto) {
        // DB에서 인코딩된 비밀번호 조회 (없으면 예외 발생)
        String originPassword = storeRepository.findPasswordById(dto.getStoreId())
                .orElseThrow(() -> new StoreException(ErrorCode.STORE_NOT_FOUND, "매장을 찾을 수 없습니다."));
        // 비밀번호 비교
        if (!passwordEncoder.matches(dto.getPassword(), originPassword)) {
            throw new StoreException(ErrorCode.INVALID_PASSWORD, ErrorCode.INVALID_PASSWORD.getMessage());
        }
        // 삭제 실행
        storeRepository.deleteStore(dto.getStoreId());
    }

    // 토큰이 유효한지 검증, 유효하다면 Bearer를 제거한 토큰을 리턴
    private void validateToken(Long storeId, HttpServletRequest request) {
        // Authorization 헤더에서 토큰 추출 (Bearer 토큰)
        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new UserException(ErrorCode.UNAUTHORIZED, "접근 권한이 없습니다.");
        }
        validateUserAuthority(storeId, authHeader.substring(7));
    }

    // 토큰에 저장된 storeId들이 유효한지 검증
    private void validateUserAuthority(Long storeId, String token) {
        // JWT 토큰 파싱하여 Claims 가져오기
        Claims claims = jwtTokenProvider.parseClaims(token);
        // "storeIds" claim에 저장된 매장 ID 리스트 추출
        List<?> storeIds = claims.get("storeIds", List.class);
        log.info("storeIds!!!: {}", storeIds);
        if (storeIds == null ||
                storeIds.stream().noneMatch(id -> id.toString().equals(storeId.toString()))) {
            throw new StoreException(ErrorCode.UNAUTHORIZED, "해당 매장에 대한 접근 권한이 없습니다.");
        }
    }
}