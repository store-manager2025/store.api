package com.project.storemanager_api.repository;

import com.project.storemanager_api.domain.place.entity.SavePlaceRequestDto;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface PlaceRepository {

    // store안에 장소가 이미 있는지 storeId로 검증
    List<String> findListById(Long storeId);


    void savePlace(SavePlaceRequestDto dto);
}
