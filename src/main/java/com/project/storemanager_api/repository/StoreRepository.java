package com.project.storemanager_api.repository;

import com.project.storemanager_api.domain.store.dto.request.SaveStoreRequestDto;
import com.project.storemanager_api.domain.store.dto.response.StoreResponseDto;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface StoreRepository {

    // 매장 생성
    void saveStore(SaveStoreRequestDto dto);

    // 한 유저의 매장 리스트 출력
    List<StoreResponseDto> findStoreList(Long userId);

}
