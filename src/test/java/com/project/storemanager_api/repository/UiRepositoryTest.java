package com.project.storemanager_api.repository;

import com.project.storemanager_api.domain.ui.entity.UiLayout;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
@Rollback
class UiRepositoryTest {
    
    @Autowired
    UiRepository uiRepository;
    
    
    @Test
    @DisplayName("뱉어내는 id값 받기")
    void getIdTest() {
        UiLayout layout = new UiLayout();
        layout.setStoreId(6L);

        // insert 수행
        uiRepository.saveUi(layout);

        // saveUi 완료 후, MyBatis가 자동 생성된 ui_id를 layout.uiId에 대입
        Long generatedUiId = layout.getUiId();
        System.out.println("generatedUiId = " + generatedUiId);

    }
    

}