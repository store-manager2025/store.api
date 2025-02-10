package com.project.storemanager_api.service;

import com.project.storemanager_api.domain.category.dto.request.SaveCategoryDto;
import com.project.storemanager_api.domain.category.dto.response.CategoryResponseDto;
import com.project.storemanager_api.domain.store.dto.response.StoreDetailResponseDto;
import com.project.storemanager_api.domain.ui.dto.response.UiResponseDto;
import com.project.storemanager_api.domain.ui.entity.UiLayout;
import com.project.storemanager_api.exception.*;
import com.project.storemanager_api.repository.CategoryRepository;
import com.project.storemanager_api.repository.StoreRepository;
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
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final UiRepository uiRepository;
    private final StoreRepository storeRepository;

    public CategoryResponseDto saveCategory(SaveCategoryDto dto) {

        // storeId가 유효한지 검사
        extracted(dto.getStoreId());

        // 입력값 검증
        if (dto.getCategoryName().isEmpty()) {
            throw new MenuException(ErrorCode.EMPTY_DATA, "값을 모두 입력해주세요.");
        }

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
        categoryRepository.saveCategory(dto);

        // 3. menu 객체를 저장 후 생성된 id를 받아온다
        Long generatedCategoryId = dto.getCategoryId();
        log.info("방금 save된 menu_id = {}", generatedCategoryId);

        return CategoryResponseDto.toResponseDto(generatedCategoryId, generatedUiId, dto, newUi);
    }


    public List<CategoryResponseDto> getAllCategories(Long storeId) {
        // storeId가 유효한지 검사
        extracted(storeId);
        List<CategoryResponseDto> categoryResponseDtos = categoryRepository.findListByStoreId(storeId);
        if (categoryResponseDtos.isEmpty()) {
            throw new CategoryException(ErrorCode.INVALID_ID, ErrorCode.INVALID_ID.getMessage());
        }

        // ui id를 가지고 dto에 담는 로직
        for (CategoryResponseDto dto : categoryResponseDtos) {
            UiLayout foundUi = uiRepository.findById(dto.getUiId())
                    .orElseThrow(() -> new UiException(ErrorCode.INVALID_ID, ErrorCode.INVALID_ID.getMessage()));
            dto.setCategoryStyle(UiResponseDto.toResponseDto(dto.getUiId(), foundUi));
        }

        return  categoryResponseDtos;
    }


    // 매장 정보가 조회되는지 안되는지 검사
    private StoreDetailResponseDto extracted(Long storeId) {

        StoreDetailResponseDto exist = storeRepository.findStoreDetailByStoreId(storeId);

        if (exist == null) {
            throw new StoreException(ErrorCode.STORE_NOT_FOUND, "매장 정보를 찾을 수 없습니다.");
        }
        return exist;
    }


}
