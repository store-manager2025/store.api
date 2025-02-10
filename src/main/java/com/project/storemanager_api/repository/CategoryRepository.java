package com.project.storemanager_api.repository;

import com.project.storemanager_api.domain.category.dto.request.SaveCategoryDto;
import com.project.storemanager_api.domain.category.dto.response.CategoryResponseDto;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface CategoryRepository {

    void saveCategory(SaveCategoryDto dto);

    List<CategoryResponseDto> findListByStoreId(Long storeId);
}
