package com.project.storemanager_api.repository;

import com.project.storemanager_api.domain.report.dto.response.StoreTimeResponseDto;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface StoreOperateTimeRepository {


    void insertOpenStore(Long storeId); // 새로운 row 추가
    void closeStore(Long storeId);      // 오늘 날짜의 row에 closed_at 업데이트

    List<StoreTimeResponseDto> getStoreOperateInfo(Long userId); // 오픈 시간 조회
}
