package com.project.storemanager_api.controller;

import com.project.storemanager_api.domain.place.dto.request.ModifyPlaceRequestDto;
import com.project.storemanager_api.domain.place.dto.request.SavePlaceRequestDto;
import com.project.storemanager_api.domain.place.dto.response.PlaceResponseDto;
import com.project.storemanager_api.service.PlaceService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@Slf4j
@RequestMapping("/places")
@RequiredArgsConstructor
public class PlaceController {

    private final PlaceService placeService;

    @PostMapping
    public ResponseEntity<Map<String, Object>> createPlace(@RequestBody SavePlaceRequestDto dto) {
        log.info("save place dto : {} ", dto);
        placeService.savePlace(dto);
        return ResponseEntity.ok().body(Map.of(
                "message", "장소가 성공적으로 등록되었습니다."
        ));
    }

    @GetMapping("/all/{storeId}")
    public ResponseEntity<List<PlaceResponseDto>> getAllPlaces(@PathVariable Long storeId) {
        log.info("get all places : {}", storeId);
        List<PlaceResponseDto> result = placeService.getPlaces(storeId);
        return ResponseEntity.ok().body(result);
    }

    @GetMapping("/{placeId}")
    public ResponseEntity<PlaceResponseDto> getPlace(@PathVariable Long placeId) {
        log.info("get place : {}", placeId);
        PlaceResponseDto result = placeService.getPlace(placeId);
        return ResponseEntity.ok().body(result);
    }

    // 좌석 이름, ui 수정
    @PatchMapping
    public ResponseEntity<Map<String, Object>> updatePlace(@RequestBody ModifyPlaceRequestDto dto) {
        log.info("update place dto : {}", dto);
        placeService.modifyPlace(dto);
        return ResponseEntity.ok().body(Map.of(
                "message", "성공적으로 수정되었습니다."
        ));
    }

}
