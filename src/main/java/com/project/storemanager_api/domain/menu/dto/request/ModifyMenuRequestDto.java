package com.project.storemanager_api.domain.menu.dto.request;

import lombok.*;

@Getter
@Setter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ModifyMenuRequestDto {

    private Long menuId;
    private Long uiId;

    // 수정 가능한 값들
    private String menuName;
    private Integer discountRate;
    private Integer price;
    private String colorCode;
    private Integer positionX;
    private Integer positionY;
    private String sizeType;
}
