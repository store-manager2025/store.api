package com.project.storemanager_api.repository;

import com.project.storemanager_api.domain.menu.dto.request.SaveMenuRequestDto;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface MenuRepository {

    void saveMenu(SaveMenuRequestDto dto);

}
