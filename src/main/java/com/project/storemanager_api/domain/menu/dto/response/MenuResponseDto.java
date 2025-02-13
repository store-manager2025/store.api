package com.project.storemanager_api.domain.menu.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.project.storemanager_api.domain.ui.dto.response.UiResponseDto;
import lombok.*;

@Getter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MenuResponseDto {

    private Long menuId;
    private Long categoryId;
    private String menuName;
    private int discountRate; // 할인률
    private int price;

    @JsonIgnore
    private Long uiId;

    @Setter
    private UiResponseDto menuStyle;

}
