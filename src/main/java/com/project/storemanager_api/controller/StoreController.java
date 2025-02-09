package com.project.storemanager_api.controller;

import com.project.storemanager_api.domain.store.dto.request.SaveStoreRequestDto;
import com.project.storemanager_api.domain.store.dto.response.StoreResponseDto;
import com.project.storemanager_api.service.StoreService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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
    public ResponseEntity<Map<String, Object>> createStore(@AuthenticationPrincipal Long userId, @RequestBody SaveStoreRequestDto dto) {
        storeService.saveStore(dto, userId);
        return ResponseEntity.ok().body(Map.of(
                "message", "매장이 성공적으로 생성 되었습니다."
        ));
    }

    /**
     * 한 유저가 등록한 모든 매장목록 조회 API
     * @param userId - 요청 헤더에 들어있는 token을 파싱한 id
     * @return responseDto로 변환된 store정보를 담은 객체
     */
    @GetMapping
    public ResponseEntity<List<StoreResponseDto>> getStores(@AuthenticationPrincipal Long userId) {
        log.info("인증된 사용자의 Get요청  id : {} ", userId);
        List<StoreResponseDto> storeList = storeService.getStoreList(userId);
        return ResponseEntity.ok().body(storeList);
    }

}
