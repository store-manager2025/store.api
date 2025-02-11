package com.project.storemanager_api.controller;

import com.project.storemanager_api.domain.category.dto.request.SaveCategoryDto;
import com.project.storemanager_api.domain.category.dto.response.CategoryResponseDto;
import com.project.storemanager_api.service.CategoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/categories")
public class CategoryController {

    private final CategoryService categoryService;

    // 생성
    @PostMapping
    public ResponseEntity<Map<String, Object>> saveCategory(@RequestBody SaveCategoryDto dto) {
        log.info("Saving category: {}", dto);
        categoryService.saveCategory(dto);
        return ResponseEntity.ok().body(Map.of(
                "message", "카테고리가 생성되었습니다."
        ));
    }

    // 한 매장에 등록된 category 전체 조회
    @GetMapping("/all/{storeId}")
    public ResponseEntity<List<CategoryResponseDto>> getAllCategories(@PathVariable Long storeId) {
        log.info("Getting all categories: {}", storeId);
        List<CategoryResponseDto> result = categoryService.getAllCategories(storeId);
        return ResponseEntity.ok().body(result);
    }

    // category 단일 조회
    @GetMapping("/{categoryId}")
    public ResponseEntity<CategoryResponseDto> getCategory(@PathVariable Long categoryId) {
        log.info("Getting category: {}", categoryId);
        CategoryResponseDto result = categoryService.getCategory(categoryId);
        return ResponseEntity.ok().body(result);
    }

    // 카테고리 수정
//    @PatchMapping
//    public ResponseEntity<?> modifyCategory() {
//
//    }
}
