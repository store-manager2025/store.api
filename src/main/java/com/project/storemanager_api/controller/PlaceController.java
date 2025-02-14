package com.project.storemanager_api.controller;

import com.project.storemanager_api.domain.place.entity.SavePlaceRequestDto;
import com.project.storemanager_api.service.PlaceService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@Slf4j
@RequestMapping("/places")
@RequiredArgsConstructor
public class PlaceController {

    private final PlaceService placeService;

    @PostMapping
    public ResponseEntity<?> createPlace(@RequestBody SavePlaceRequestDto dto) {
        log.info("save place dto : {} ", dto);
        placeService.savePlace(dto);
        return ResponseEntity.ok().body(Map.of(
                "message", "장소가 성공적으로 등록되었습니다."
        ));
    }

}
