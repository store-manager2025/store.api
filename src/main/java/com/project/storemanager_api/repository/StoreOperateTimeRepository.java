package com.project.storemanager_api.repository;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Repository
@Mapper
public interface StoreOperateTimeRepository {


    void insertOpenStore(Long storeId); // 새로운 row 추가
    void closeStore(Long storeId);      // 오늘 날짜의 row에 closed_at 업데이트

}
