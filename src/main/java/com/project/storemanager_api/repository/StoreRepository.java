package com.project.storemanager_api.repository;

import com.project.storemanager_api.domain.store.dto.request.ModifyStoreRequestDto;
import com.project.storemanager_api.domain.store.dto.request.SaveStoreRequestDto;
import com.project.storemanager_api.domain.store.dto.response.StoreDetailResponseDto;
import com.project.storemanager_api.domain.store.dto.response.StoreResponseDto;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Mapper
@Repository
public interface StoreRepository {

    // 매장 생성
    void saveStore(SaveStoreRequestDto dto);

    // 한 유저의 매장 리스트 출력
    List<StoreResponseDto> findStoreList(Long userId);

    // 매장 존재 여부 조회 - 성공하면 password 가져옴
    Optional<String> findPasswordById(Long storeId);

    // 매장 단일 상세 조회
    StoreDetailResponseDto findStoreDetailByStoreId(Long storeId);

    void updateStore(ModifyStoreRequestDto dto);

    void deleteStore(Long storeId);


}
