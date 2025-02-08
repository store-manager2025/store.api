package com.project.storemanager_api.controller;

import com.project.storemanager_api.domain.store.dto.request.SaveStoreRequestDto;
import com.project.storemanager_api.service.StoreService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@Slf4j
@RequestMapping("/api/stores")
@RequiredArgsConstructor
public class StoreController {

    private final StoreService storeService;

    // 새 매장 생성
    @PostMapping
    public ResponseEntity<?> createStore(@AuthenticationPrincipal Long userId, @RequestBody SaveStoreRequestDto dto) {
        log.info("인증된 사용자의 post요청  id : {} ", userId);
        log.info("Creating a new store: {}", dto);
        storeService.saveStore(dto, userId);
        return ResponseEntity.ok().body(Map.of(
                "message", "매장이 성공적으로 생성 되었습니다."
        ));
    }
}
