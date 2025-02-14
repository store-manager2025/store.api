package com.project.storemanager_api.validator;

import com.project.storemanager_api.domain.category.dto.request.ModifyCategoryRequestDto;
import org.springframework.stereotype.Component;

@Component
public class CategoryValidator {


    /**
     * 카테고리 수정,
     * 비어있는 필드가 있으면 기존값 그대로 사용
     * @param originalData  db에서 가져온 기존 값
     * @param dto           수정 요청 DTO
     */
    public void prepareModifyCategory(ModifyCategoryRequestDto originalData, ModifyCategoryRequestDto dto) {

        // categoryName: 값이 비어있으면 기존 값 사용
        if (dto.getCategoryName() == null || dto.getCategoryName().trim().isEmpty()) {
            dto.setCategoryName(originalData.getCategoryName());
        }
        if (dto.getColorCode() == null || dto.getColorCode().trim().isEmpty()) {
            dto.setColorCode(originalData.getColorCode());
        }
        if (dto.getPositionX() == null) {
            dto.setPositionX(originalData.getPositionX());
        }
        if (dto.getPositionY() == null) {
            dto.setPositionY(originalData.getPositionY());
        }
        if (dto.getSizeType() == null || dto.getSizeType().trim().isEmpty()) {
            dto.setSizeType(originalData.getSizeType());
        }
    }

}
