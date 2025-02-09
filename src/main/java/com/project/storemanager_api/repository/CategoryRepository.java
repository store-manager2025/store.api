package com.project.storemanager_api.repository;

import com.project.storemanager_api.domain.category.dto.request.SaveCategoryDto;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface CategoryRepository {

    void saveCategory(SaveCategoryDto dto);
}
