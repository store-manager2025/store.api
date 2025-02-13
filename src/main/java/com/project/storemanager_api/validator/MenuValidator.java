package com.project.storemanager_api.validator;

import com.project.storemanager_api.domain.menu.dto.request.ModifyMenuRequestDto;
import org.springframework.stereotype.Component;

@Component
public class MenuValidator {

    /**
     * 메뉴 수정,
     * 비어있는 필드가 있으면 기존값 그대로 사용
     * @param originalData  db에서 가져온 기존 값
     * @param dto           수정 요청 DTO
     */
    public void prepareModifyMenu(ModifyMenuRequestDto originalData, ModifyMenuRequestDto dto) {
        // 값이 비어있으면 기존 값 사용
        if (dto.getMenuName() == null || dto.getMenuName().trim().isEmpty()) {
            dto.setMenuName(originalData.getMenuName());
        }
        if (dto.getDiscountRate() == null) {
            dto.setDiscountRate(originalData.getDiscountRate());
        }
        if (dto.getPrice() == null) {
            dto.setPrice(originalData.getPrice());
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
        dto.setUiId(originalData.getUiId());
    }
}
