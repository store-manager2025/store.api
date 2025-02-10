package com.project.storemanager_api.service;

import com.project.storemanager_api.domain.category.dto.response.CategoryResponseDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
class CategoryServiceTest {

    @Autowired
    CategoryService categoryService;


    @Test
    @DisplayName("모든카테고리 조회")
    void getAllCategories() {
        //given
        Long storeId = 10L;
        //when
        List<CategoryResponseDto> allCategories = categoryService.getAllCategories(storeId);
        //then
        allCategories.forEach(System.out::println);
    }


}