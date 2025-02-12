package com.project.storemanager_api.domain.category.dto.request;

import lombok.*;

@Getter
@Setter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ModifyCategoryRequestDto {

    private Long categoryId;
    private Long uiId;

    // 수정 가능한 값들
    private String categoryName;
    private String colorCode;
    private Integer positionX;
    private Integer positionY;
    private String sizeType;

}
