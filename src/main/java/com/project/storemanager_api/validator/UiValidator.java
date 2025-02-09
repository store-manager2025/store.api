package com.project.storemanager_api.validator;

import com.project.storemanager_api.domain.ui.dto.request.ChangeStyleRequestDto;
import com.project.storemanager_api.domain.ui.entity.UiLayout;
import org.springframework.stereotype.Component;

@Component
public class UiValidator {


    /**
     * 클라이언트에서 비어있는 필드가 있으면, 기존 값을 그대로 사용하고,
     * 비밀번호가 새로 입력된 경우에는 기존과 동일한지 비교 후, 다르면 인코딩하여 설정
     * @param dto          수정 요청 DTO
     * @param foundUi      기존 값 대조를 위한 원래 객체
     */
    public void prepareModifyUiInput(ChangeStyleRequestDto dto, UiLayout foundUi) {
        // colorCode: 값이 없으면 기존 값 사용
        if (dto.getColorCode() == null || dto.getColorCode().trim().isEmpty()) {
            dto.setColorCode(foundUi.getColorCode());
        }
        if (dto.getSizeType() == null || dto.getSizeType().trim().isEmpty()) {
            dto.setSizeType(foundUi.getSizeType());
        }
        // positionX: 값이 없으면 기존 값 사용
        if (dto.getPositionX() == null) {
            dto.setPositionX(foundUi.getPositionX());
        }
        if (dto.getPositionY() == null) {
            dto.setPositionY(foundUi.getPositionY());
        }
    }
}
