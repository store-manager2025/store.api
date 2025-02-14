package com.project.storemanager_api.validator;

import com.project.storemanager_api.domain.place.dto.request.ModifyPlaceRequestDto;
import com.project.storemanager_api.exception.ErrorCode;
import com.project.storemanager_api.exception.PlaceException;
import org.springframework.stereotype.Component;

@Component
public class PlaceValidator {


    /**
     * 좌석 수정,
     * 비어있는 필드가 있으면 기존값 그대로 사용
     * @param originalData  db에서 가져온 기존 값
     * @param dto           수정 요청 DTO
     */
    public void prepareModifyPlace(ModifyPlaceRequestDto originalData, ModifyPlaceRequestDto dto) {

        // 모두 비어있으면
        if (dto.getPlaceName() == null || dto.getPlaceName().trim().isEmpty()
                && dto.getSizeType() == null || dto.getSizeType().trim().isEmpty()) {
            throw new PlaceException(ErrorCode.EMPTY_DATA, "한개의 값이라도 입력해야 합니다.");
        }

        if (dto.getPlaceName() == null || dto.getPlaceName().trim().isEmpty()) {
            dto.setPlaceName(originalData.getPlaceName());
        }
        if (dto.getSizeType() == null || dto.getSizeType().trim().isEmpty()) {
            dto.setSizeType(originalData.getSizeType());
        }
        dto.setUiId(originalData.getUiId());
    }
}
