package com.project.storemanager_api.service;

import com.project.storemanager_api.domain.menu.dto.request.SaveMenuRequestDto;
import com.project.storemanager_api.domain.store.dto.response.StoreDetailResponseDto;
import com.project.storemanager_api.domain.ui.entity.UiLayout;
import com.project.storemanager_api.exception.ErrorCode;
import com.project.storemanager_api.exception.MenuException;
import com.project.storemanager_api.exception.StoreException;
import com.project.storemanager_api.repository.MenuRepository;
import com.project.storemanager_api.repository.StoreRepository;
import com.project.storemanager_api.repository.UiRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@Slf4j
@RequiredArgsConstructor
public class MenuService {

    private final MenuRepository menuRepository;
    private final StoreRepository storeRepository;
    private final UiRepository uiRepository;

    public void saveMenu(SaveMenuRequestDto dto) {

        StoreDetailResponseDto exist = storeRepository.findStoreDetailByStoreId(dto.getStoreId());
        if (exist == null) {
            throw new StoreException(ErrorCode.STORE_NOT_FOUND, "매장 정보를 찾을 수 없습니다.");
        }

        if (dto.getMenuName().isEmpty() || dto.getPrice() == null) {
            throw new MenuException(ErrorCode.EMPTY_DATA, "값을 모두 입력해주세요.");
        }

        // storeId가 있어야 ui에 insert를 하고 생성된 uiId를 받을 수 있다.
        UiLayout newUi = UiLayout.builder()
                .storeId(dto.getStoreId())
                .build();

        uiRepository.saveUi(newUi);
        Long generatedUiId = newUi.getUiId();
        log.info("방금 save된 ui id - {} ", generatedUiId);
        dto.setUiId(generatedUiId);
        menuRepository.saveMenu(dto);

    }
}
