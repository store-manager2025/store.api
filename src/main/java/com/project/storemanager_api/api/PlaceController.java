package com.project.storemanager_api.api;

import com.project.storemanager_api.domain.place.dto.request.ModifyPlaceRequestDto;
import com.project.storemanager_api.domain.place.dto.request.SavePlaceRequestDto;
import com.project.storemanager_api.domain.place.dto.response.PlaceResponseDto;
import com.project.storemanager_api.service.PlaceService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

import static com.project.storemanager_api.util.Constants.MESSAGE;

@RestController
@Slf4j
@RequestMapping("/api/places")
@RequiredArgsConstructor
public class PlaceController {

    private final PlaceService placeService;

    @PostMapping
    public ResponseEntity<Map<String, Object>> createPlace(@RequestBody SavePlaceRequestDto dto) {
        log.info("save place dto : {} ", dto);
        placeService.savePlace(dto);
        return ResponseEntity.ok().body(Map.of(
                MESSAGE, "장소가 성공적으로 등록되었습니다."
        ));
    }

    @GetMapping("/all/{storeId}")
    @Cacheable(value = "places", key = "#storeId")
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
    @PatchMapping           // storeId를 DB에서 조회한 후 @CacheEvict의 key로 사용하면 됨.
    @CachePut(value = "places", key = "#placeService.getStoreIdByPlaceId(dto.placeId)")
    public ResponseEntity<Map<String, Object>> updatePlace(@RequestBody ModifyPlaceRequestDto dto) {
        log.info("update place dto : {}", dto);
        placeService.modifyPlace(dto);
        return ResponseEntity.ok().body(Map.of(
                MESSAGE, "성공적으로 수정되었습니다."
        ));
    }

    @DeleteMapping("/{placeId}")
    @CacheEvict(value = "places", key = "#placeService.getStoreIdByPlaceId(placeId)")
    public ResponseEntity<Map<String, Object>> deletePlace(@PathVariable Long placeId) {
        log.info("delete place : {}", placeId);
        placeService.deletePlace(placeId);
        return ResponseEntity.ok().body(Map.of(
                MESSAGE, "좌석이 성공적으로 삭제되었습니다."
        ));
    }

}
