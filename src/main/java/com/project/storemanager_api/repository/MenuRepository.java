package com.project.storemanager_api.repository;

import com.project.storemanager_api.domain.menu.dto.request.SaveMenuRequestDto;
import com.project.storemanager_api.domain.menu.dto.response.MenuResponseDto;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface MenuRepository {

    void saveMenu(SaveMenuRequestDto dto);

    // 생성해야 함
    List<MenuResponseDto> findListByCategoryId(Long categoryId);
}
