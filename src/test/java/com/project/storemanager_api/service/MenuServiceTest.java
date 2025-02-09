package com.project.storemanager_api.service;

import com.project.storemanager_api.domain.menu.dto.request.SaveMenuRequestDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
@Rollback
class MenuServiceTest {

    @Autowired
    MenuService menuService;


    @Test
    @DisplayName("ui 뜯어보기")
    void getUiInfo() {
        //given
        SaveMenuRequestDto build = SaveMenuRequestDto.builder()
                .storeId(6L)
                .menuName("빅맥")
                .price(4000)
                .build();
        //when
//        UiLayout uiLayout = menuService.saveMenu(build);
//        System.out.println("uiLayout = " + uiLayout);

        //then
    }


}