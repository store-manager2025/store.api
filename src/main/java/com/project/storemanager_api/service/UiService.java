package com.project.storemanager_api.service;


import com.project.storemanager_api.domain.ui.dto.request.ChangeStyleRequestDto;
import com.project.storemanager_api.domain.ui.entity.UiLayout;
import com.project.storemanager_api.exception.ErrorCode;
import com.project.storemanager_api.exception.UiException;
import com.project.storemanager_api.repository.UiRepository;
import com.project.storemanager_api.validator.UiValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@Slf4j
@RequiredArgsConstructor
public class UiService {

    private final UiRepository uiRepository;
    private final UiValidator validator;

    /**
     * 스타일을 변경하는 메서드
     *
     * @param dto positionX, positionY, colorCode, sizeType, uiId
     * @return 요청대로 바뀐 ChangeStyleRequestDto
     */
    public ChangeStyleRequestDto changeStyle(ChangeStyleRequestDto dto) {
        // 1. 기존 ui 조회
        UiLayout foundUi = uiRepository.findById(dto.getUiId())
                .orElseThrow(() -> new UiException(ErrorCode.UI_NOT_FOUND, ErrorCode.UI_NOT_FOUND.getMessage()));
        // 2. null이 들어온 필드는 기존 값 사용
        validator.prepareModifyUiInput(dto, foundUi);

        // 3. update로직
        uiRepository.updateUi(dto);
        return dto;
    }
}
