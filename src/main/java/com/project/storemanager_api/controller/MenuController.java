//package com.project.storemanager_api.controller;
//
//
//import com.project.storemanager_api.domain.menu.dto.request.SaveMenuRequestDto;
//import com.project.storemanager_api.domain.menu.dto.response.MenuResponseDto;
//import com.project.storemanager_api.service.MenuService;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.List;
//
//@RestController
//@Slf4j
//@RequiredArgsConstructor
//@RequestMapping("/api/menus")
//public class MenuController {
//
//    private final MenuService menuService;
//
//    @PostMapping
//    public ResponseEntity<MenuResponseDto> createMenu(@RequestBody SaveMenuRequestDto dto) {
//        log.info("Creating menu dto: {}", dto);
//        MenuResponseDto menuResponseDto = menuService.saveMenu(dto);
//        return ResponseEntity.ok().body(menuResponseDto);
//    }
//
//    @GetMapping
//    public ResponseEntity<List<MenuResponseDto>> getAllMenus(@RequestParam Long storeId) {
//        log.info("Getting all menus for {}", storeId);
//        return null;
//    }
//
//
//}
