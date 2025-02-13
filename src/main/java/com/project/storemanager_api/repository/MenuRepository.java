package com.project.storemanager_api.repository;

import com.project.storemanager_api.domain.menu.dto.request.ModifyMenuRequestDto;
import com.project.storemanager_api.domain.menu.dto.request.SaveMenuRequestDto;
import com.project.storemanager_api.domain.menu.dto.response.MenuResponseDto;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Mapper
@Repository
public interface MenuRepository {

    void saveMenu(SaveMenuRequestDto dto);

    List<MenuResponseDto> findListByCategoryId(Long categoryId);

    Optional<MenuResponseDto> findById(Long menuId);

    // UI와 JOIN하여 수정용 dto와 대조할 데이터를 뽑아옴
    Optional<ModifyMenuRequestDto> findModifyDtoById(Long menuId);

    void updateMenu(ModifyMenuRequestDto dto);

    void deleteMenuById(Long menuId);
}
