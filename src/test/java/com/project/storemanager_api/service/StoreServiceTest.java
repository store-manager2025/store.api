package com.project.storemanager_api.service;

import com.project.storemanager_api.domain.store.dto.response.StoreResponseDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
class StoreServiceTest {

    @Autowired
    private StoreService storeService;

    @Test
    @DisplayName("매장 전체 조회")
    void getStoreList() {

        //given
        Long userId = 11L;
        List<StoreResponseDto> storeList = storeService.getStoreList(userId);
        storeList.forEach(System.out::println);

    }
}