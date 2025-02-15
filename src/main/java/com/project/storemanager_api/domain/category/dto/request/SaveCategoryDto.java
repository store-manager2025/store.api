package com.project.storemanager_api.domain.category.dto.request;

import lombok.*;

@Setter
@Getter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SaveCategoryDto {

    private Long categoryId;
    private Long storeId;
    private Long uiId;
    private String categoryName;

    // ui
    private String colorCode;
    private Integer positionX;
    private Integer positionY;
    private String sizeType;


}
