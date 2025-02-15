package com.project.storemanager_api.repository;

import com.project.storemanager_api.domain.category.dto.request.ModifyCategoryRequestDto;
import com.project.storemanager_api.domain.category.dto.request.SaveCategoryDto;
import com.project.storemanager_api.domain.category.dto.response.CategoryResponseDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Mapper
@Repository
public interface CategoryRepository {

    void saveCategory(SaveCategoryDto dto);

    List<CategoryResponseDto> findListByStoreId(Long storeId);

    Optional<CategoryResponseDto> findById(Long categoryId);

    // UI와 JOIN하여 수정용 dto와 대조할 데이터를 뽑아옴
    Optional<ModifyCategoryRequestDto> findModifyDtoById(Long categoryId);

    void deleteCategoryById(Long categoryId);

    // 수정
    void modifyCategory(@Param("categoryName") String categoryName,@Param("categoryId") Long categoryId);
}
