package com.project.storemanager_api.controller;


import com.project.storemanager_api.domain.menu.dto.request.SaveMenuRequestDto;
import com.project.storemanager_api.service.MenuService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/menus")
public class MenuController {

    private final MenuService menuService;

    @PostMapping
    public ResponseEntity<Map<String, Object>> createMenu(@RequestBody SaveMenuRequestDto dto) {
        log.info("Creating menu dto: {}", dto);
        menuService.saveMenu(dto);
        return ResponseEntity.ok().body(Map.of(
                "message", "성공적으로 메뉴가 등록되었습니다."
        ));
    }

//    @GetMapping
//    public ResponseEntity<List<MenuResponseDto>> getAllMenus(@RequestParam Long storeId) {
//        log.info("Getting all menus for {}", storeId);
//        return null;
//    }


}
