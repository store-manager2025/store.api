package com.project.storemanager_api.service;

import com.project.storemanager_api.domain.place.entity.SavePlaceRequestDto;
import com.project.storemanager_api.domain.ui.entity.UiLayout;
import com.project.storemanager_api.exception.ErrorCode;
import com.project.storemanager_api.exception.PlaceException;
import com.project.storemanager_api.repository.PlaceRepository;
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
public class PlaceService {

    private final PlaceRepository placeRepository;
    private final UiRepository uiRepository;

    public void savePlace(SavePlaceRequestDto dto) {
        // storeId로 이미 있는 장소인지 검증
        List<String> nameList = placeRepository.findListById(dto.getStoreId());
        if (!nameList.isEmpty()) {
            for (String name : nameList) {
                if (name.equals(dto.getPlaceName())) {
                    throw new PlaceException(ErrorCode.DUPLICATE_PLACE, ErrorCode.DUPLICATE_PLACE.getMessage());
                }
            }
        }

        // ui 하나 생성
        UiLayout newUi = UiLayout.builder()
                .storeId(dto.getStoreId())
                .colorCode("#FAFAFA") // 기본값
                .build();

        uiRepository.saveUi(newUi);
        Long generatedUiId = newUi.getUiId();
        log.info("방금 save된 ui id - {} ", generatedUiId);

        dto.setUiId(generatedUiId);
        placeRepository.savePlace(dto);

    }
}
