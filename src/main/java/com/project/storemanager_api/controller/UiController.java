package com.project.storemanager_api.controller;

import com.project.storemanager_api.domain.ui.dto.request.ChangeStyleRequestDto;
import com.project.storemanager_api.service.UiService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/ui")
public class UiController {

    private final UiService uiService;

    // ui의 스타일을 변경하는 요청
    @PatchMapping
    public ResponseEntity<Map<String, Object>> changeStyle(@RequestBody ChangeStyleRequestDto dto) {
        log.info("Change style request: {}", dto);
        uiService.changeStyle(dto);
        return ResponseEntity.ok().body(Map.of(
                "message", "스타일이 성공적으로 변경되었습니다."
        ));
    }

}
