package com.project.storemanager_api.controller;


import com.project.storemanager_api.domain.menu.dto.request.ModifyMenuRequestDto;
import com.project.storemanager_api.domain.menu.dto.request.SaveMenuRequestDto;
import com.project.storemanager_api.domain.menu.dto.response.MenuResponseDto;
import com.project.storemanager_api.service.MenuService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
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

    // categoryId로 메뉴목록을 가져오는 API
    @GetMapping("/all/{categoryId}")
    public ResponseEntity<List<MenuResponseDto>> getAllMenus(@PathVariable Long categoryId) {
        log.info("Getting all menus for {}", categoryId);
        List<MenuResponseDto> result = menuService.getAllMenus(categoryId);
        return ResponseEntity.ok().body(result);
    }

    @GetMapping("/{menuId}")
    public ResponseEntity<MenuResponseDto> getMenu(@PathVariable Long menuId) {
        log.info("Getting menu for {}", menuId);
        MenuResponseDto result = menuService.getMenu(menuId);
        return ResponseEntity.ok().body(result);
    }

    @PatchMapping
    public ResponseEntity<Map<String, Object>> updateMenu(@RequestBody ModifyMenuRequestDto dto) {
        log.info("Updating menu for {}", dto);
        menuService.modifyMenu(dto);
        return ResponseEntity.ok().body(Map.of(
                "message", "성공적으로 수정되었습니다."
        ));
    }

    @DeleteMapping("/{menuId}")
    public ResponseEntity<Map<String, Object>> deleteMenu(@PathVariable Long menuId) {
        log.info("Deleting category: {}", menuId);
        menuService.deleteMenu(menuId);
        return ResponseEntity.ok().body(Map.of(
                "message", "성공적으로 삭제되었습니다."
        ));
    }



}
