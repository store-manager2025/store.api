package com.project.storemanager_api.service;

import com.project.storemanager_api.domain.menu.dto.request.ModifyMenuRequestDto;
import com.project.storemanager_api.domain.menu.dto.request.SaveMenuRequestDto;
import com.project.storemanager_api.domain.menu.dto.response.MenuResponseDto;
import com.project.storemanager_api.domain.ui.dto.request.ChangeStyleRequestDto;
import com.project.storemanager_api.domain.ui.dto.response.UiResponseDto;
import com.project.storemanager_api.domain.ui.entity.UiLayout;
import com.project.storemanager_api.exception.CategoryException;
import com.project.storemanager_api.exception.ErrorCode;
import com.project.storemanager_api.exception.MenuException;
import com.project.storemanager_api.exception.UiException;
import com.project.storemanager_api.repository.CategoryRepository;
import com.project.storemanager_api.repository.MenuRepository;
import com.project.storemanager_api.repository.UiRepository;
import com.project.storemanager_api.validator.MenuValidator;
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
    private final CategoryService categoryService;
    private final MenuValidator menuValidator;

    public void saveMenu(SaveMenuRequestDto dto) {

        // storeId가 있어야 ui에 insert를 하고 생성된 uiId를 받을 수 있다.
        Integer positionX = dto.getPositionX() != null ? dto.getPositionX() : 0;
        Integer positionY = dto.getPositionY() != null ? dto.getPositionY() : 0;
        String sizeType = dto.getSizeType() != null ? dto.getSizeType() : "";
        String colorCode = dto.getColorCode() != null ? dto.getColorCode() : "#FAFAFA";

        UiLayout newUi = UiLayout.builder()
                .storeId(dto.getStoreId())
                .colorCode(colorCode) // 기본값
                .positionX(positionX)
                .positionY(positionY)
                .sizeType(sizeType)
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


    public MenuResponseDto getMenu(Long menuId) {
        MenuResponseDto foundMenu = menuRepository.findById(menuId).orElseThrow(
                () -> new MenuException(ErrorCode.INVALID_ID, ErrorCode.INVALID_ID.getMessage())
        );
        UiLayout foundUi = uiRepository.findById(foundMenu.getUiId())
                .orElseThrow(() -> new UiException(ErrorCode.INVALID_ID, ErrorCode.INVALID_ID.getMessage()));
        foundMenu.setMenuStyle(UiResponseDto.toResponseDto(foundMenu.getUiId(), foundUi));

        return foundMenu;
    }

    public void modifyMenu(ModifyMenuRequestDto dto) {
        log.info("requestDTO : {} ", dto.toString());
        ModifyMenuRequestDto originalData = menuRepository.findModifyDtoById(dto.getMenuId()).orElseThrow(
                () -> new MenuException(ErrorCode.INVALID_ID, ErrorCode.INVALID_ID.getMessage())
        );
        log.info("original Data = {}", originalData);

        menuValidator.prepareModifyMenu(originalData, dto);

        menuRepository.updateMenu(dto);

        // ui 업데이트
        uiRepository.updateUi(ChangeStyleRequestDto.builder()
                .uiId(originalData.getUiId())
                .colorCode(dto.getColorCode())
                .positionX(dto.getPositionX())
                .positionY(dto.getPositionY())
                .sizeType(dto.getSizeType())
                .build());

    }

    public void deleteMenu(Long menuId) {
        MenuResponseDto exist = menuRepository.findById(menuId).orElseThrow(
                () -> new MenuException(ErrorCode.INVALID_ID, ErrorCode.INVALID_ID.getMessage())
        );
        if (exist != null) {
            menuRepository.deleteMenuById(menuId);
        }
    }
}
