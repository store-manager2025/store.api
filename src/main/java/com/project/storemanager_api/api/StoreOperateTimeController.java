package com.project.storemanager_api.api;

import com.project.storemanager_api.annotation.StoreAuthCheck;
import com.project.storemanager_api.domain.report.dto.response.StoreTimeResponseDto;
import com.project.storemanager_api.domain.user.dto.response.CustomUserPrincipal;
import com.project.storemanager_api.service.StoreOperateTimeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/times")
public class StoreOperateTimeController {

    private final StoreOperateTimeService timeService;

    @PostMapping("/open/{storeId}")
    @StoreAuthCheck
    public ResponseEntity<Map<String, Object>> openStore(@AuthenticationPrincipal CustomUserPrincipal info,
                                                         @PathVariable Long storeId) {
        log.info("open store : {}, {}", storeId, LocalDateTime.now());
        Map<String, Object> result = timeService.openStore(storeId);
        return ResponseEntity.ok().body(result);
    }

    @PostMapping("/close/{storeId}")
    @StoreAuthCheck
    public ResponseEntity<Map<String, Object>> closeStore(@AuthenticationPrincipal CustomUserPrincipal info,
                                                          @PathVariable Long storeId) {

        log.info("close store : {}, {}", storeId, LocalDateTime.now());
        Map<String, Object> result = timeService.closeStore(storeId);
        return ResponseEntity.ok().body(result);
    }

    @GetMapping("/all-info")
    public ResponseEntity<List<StoreTimeResponseDto>> getOpenTime(@AuthenticationPrincipal CustomUserPrincipal info) {
        log.info("info.userId : {}", info.getUserId());
        List<StoreTimeResponseDto> operateTimeList = timeService.getOpenTime(info.getUserId());
        return ResponseEntity.ok().body(operateTimeList);
    }

}
