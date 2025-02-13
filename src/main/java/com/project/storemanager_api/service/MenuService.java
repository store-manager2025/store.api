package com.project.storemanager_api.service;

import com.project.storemanager_api.domain.menu.dto.request.SaveMenuRequestDto;
import com.project.storemanager_api.domain.menu.dto.response.MenuResponseDto;
import com.project.storemanager_api.domain.store.dto.response.StoreDetailResponseDto;
import com.project.storemanager_api.domain.ui.dto.response.UiResponseDto;
import com.project.storemanager_api.domain.ui.entity.UiLayout;
import com.project.storemanager_api.exception.*;
import com.project.storemanager_api.repository.CategoryRepository;
import com.project.storemanager_api.repository.MenuRepository;
import com.project.storemanager_api.repository.UiRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@Slf4j
@RequiredArgsConstructor
public class MenuService {

    private final MenuRepository menuRepository;
    private final CategoryRepository categoryRepository;
    private final UiRepository uiRepository;

    public void saveMenu(SaveMenuRequestDto dto) {

        // storeId가 있어야 ui에 insert를 하고 생성된 uiId를 받을 수 있다.
        UiLayout newUi = UiLayout.builder()
                .storeId(dto.getStoreId())
                .colorCode("#FAFAFA") // 기본값
                .build();

        // 1. ui 객체를 저장 후 생성된 id를 받아온다
        uiRepository.saveUi(newUi);
        Long generatedUiId = newUi.getUiId();
        log.info("방금 save된 ui id - {} ", generatedUiId);

        // 2. 받아온 id를 dto에 저장
        dto.setUiId(generatedUiId);
        menuRepository.saveMenu(dto);
    }


    public List<MenuResponseDto> getAllMenus(Long categoryId) {
        // 파라미터값 유효한지 검증부터
        categoryRepository.findById(categoryId)
                .orElseThrow(() -> new CategoryException(ErrorCode.INVALID_ID, "카테고리 정보를 찾을 수 없습니다."));

        // 리턴값 생성
        List<MenuResponseDto> result = menuRepository.findListByCategoryId(categoryId);

        for (MenuResponseDto dto : result) {
            UiLayout foundUi = uiRepository.findById(dto.getUiId())
                    .orElseThrow(() -> new UiException(ErrorCode.INVALID_ID, ErrorCode.INVALID_ID.getMessage()));

            dto.setMenuStyle(UiResponseDto.toResponseDto(dto.getUiId(), foundUi));
        }

        return result;
    }


    private void extracted(SaveMenuRequestDto dto, StoreDetailResponseDto exist) {
        if (exist == null) {
            throw new StoreException(ErrorCode.STORE_NOT_FOUND, "매장 정보를 찾을 수 없습니다.");
        }

        if (dto.getMenuName().isEmpty() || dto.getPrice() == null) {
            throw new MenuException(ErrorCode.EMPTY_DATA, "값을 모두 입력해주세요.");
        }
    }
}
