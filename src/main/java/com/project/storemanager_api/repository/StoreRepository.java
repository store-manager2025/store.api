package com.project.storemanager_api.repository;

import com.project.storemanager_api.domain.store.dto.request.SaveStoreRequestDto;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface StoreRepository {

    void saveStore(SaveStoreRequestDto dto);

}
