package com.project.storemanager_api.domain.category.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.project.storemanager_api.domain.category.dto.request.SaveCategoryDto;
import com.project.storemanager_api.domain.ui.dto.response.UiResponseDto;
import com.project.storemanager_api.domain.ui.entity.UiLayout;
import lombok.*;

@Getter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CategoryResponseDto {

    private Long categoryId;

    @JsonIgnore // 내보낼때 storeId는 제거
    private Long storeId;

    private String categoryName;

    @Setter
    private UiResponseDto categoryStyle;

    @JsonIgnore // JSON직렬화에서는 제외
    private Long uiId;


    public static CategoryResponseDto toResponseDto(
            Long generatedCategoryId,
            Long generatedUiId,
            SaveCategoryDto dto,
            UiLayout newUi) {

        return CategoryResponseDto.builder()
                .categoryId(generatedCategoryId)
                .storeId(dto.getStoreId())
                .categoryName(dto.getCategoryName())
                .categoryStyle(UiResponseDto.toResponseDto(generatedUiId, newUi))
                .build();

    }
}
