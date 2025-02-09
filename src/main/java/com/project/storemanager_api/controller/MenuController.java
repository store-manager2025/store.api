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

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/menus")
public class MenuController {

    private final MenuService menuService;

    @PostMapping
    public ResponseEntity<?> createMenu(@RequestBody SaveMenuRequestDto dto) {
        log.info("Creating menu dto: {}", dto);
        menuService.saveMenu(dto);
        return null;
    }


}
