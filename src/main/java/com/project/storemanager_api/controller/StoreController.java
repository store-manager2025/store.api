package com.project.storemanager_api.controller;

import com.project.storemanager_api.annotation.StoreAuthCheck;
import com.project.storemanager_api.domain.store.dto.request.DeleteStoreRequestDto;
import com.project.storemanager_api.domain.store.dto.request.ModifyStoreRequestDto;
import com.project.storemanager_api.domain.store.dto.request.SaveStoreRequestDto;
import com.project.storemanager_api.domain.store.dto.request.StoreLoginRequestDto;
import com.project.storemanager_api.domain.store.dto.response.StoreDetailResponseDto;
import com.project.storemanager_api.domain.store.dto.response.StoreResponseDto;
import com.project.storemanager_api.domain.user.dto.response.CustomUserPrincipal;
import com.project.storemanager_api.service.StoreService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@Slf4j
@RequestMapping("/api/stores")
@RequiredArgsConstructor
public class StoreController {

    private final StoreService storeService;

    // 새 매장 생성
    @PostMapping
    public ResponseEntity<Map<String, Object>> createStore(@AuthenticationPrincipal CustomUserPrincipal userInfo,
                                                           @RequestBody SaveStoreRequestDto dto) {
        Map<String, Object> messageAndToken = storeService.saveStore(dto, userInfo.getUserId());
        return ResponseEntity.ok().body(messageAndToken);
    }

    /**
     * 한 유저가 등록한 모든 매장목록 조회 API
     * @param userInfo - 요청 헤더에 들어있는 token을 파싱한 id
     * @return responseDto로 변환된 store정보를 담은 객체
     */
    @GetMapping
    public ResponseEntity<List<StoreResponseDto>> getStores(@AuthenticationPrincipal CustomUserPrincipal userInfo) {
        List<StoreResponseDto> storeList = storeService.getStoreList(userInfo.getUserId());
        return ResponseEntity.ok().body(storeList);
    }

    @PostMapping("/login")
    public ResponseEntity<StoreDetailResponseDto> loginInStore(@RequestBody StoreLoginRequestDto dto) {
        log.info("StoreLoginRequestDto : {}", dto.toString());
        StoreDetailResponseDto storeDetailResponseDto = storeService.loginInStore(dto);
        return ResponseEntity.ok().body(storeDetailResponseDto);
    }

    /**
     * 매장 정보 수정 API
     *
     * @param userInfo - 검증을 위한, 입력을 보낸 사용자의 id, storeIdList가 들어있는 객체
     * @param dto - storePlace, storeName, password를 수정할 수 있는 DTO
     * @return updatedStoreDto - 수정된 store 객체
     */
    @PatchMapping
    @StoreAuthCheck
    @PreAuthorize("hasRole('OWNER')")
    public ResponseEntity<Map<String, String>> modifyStore(@AuthenticationPrincipal CustomUserPrincipal userInfo,
                                                           @RequestBody ModifyStoreRequestDto dto) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        log.info("User role: {}", authentication.getAuthorities());
        storeService.modifyStoreInfo(dto);
        return ResponseEntity.ok().body(Map.of(
                "message", "매장이 성공적으로 수정 되었습니다."
        ));
    }

    // 매장 삭제 API
    @StoreAuthCheck
    @DeleteMapping
    @PreAuthorize("hasRole('OWNER')")
    public ResponseEntity<Map<String, Object>> deleteStore(@AuthenticationPrincipal CustomUserPrincipal userInfo,
                                                           @RequestBody DeleteStoreRequestDto dto) {
        log.info("DeleteStoreRequestDto : {}", dto);
        storeService.deleteStore(dto);
        return ResponseEntity.ok().body(Map.of(
                "message", "매장이 성공적으로 삭제 되었습니다."
        ));
    }

}
