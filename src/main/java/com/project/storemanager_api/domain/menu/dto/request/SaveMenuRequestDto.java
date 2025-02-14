package com.project.storemanager_api.domain.menu.dto.request;

import lombok.*;

@Getter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SaveMenuRequestDto {

    private Long menuId;

    @Setter
    private Long uiId;

    private Long categoryId;
    private Long storeId;
    private String menuName;
    private Integer price;
    private Integer discountRate;

    // ui
    private String colorCode;
    private Integer positionX;
    private Integer positionY;
    private String sizeType;

}
