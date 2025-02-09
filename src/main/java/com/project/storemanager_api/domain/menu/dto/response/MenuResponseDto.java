package com.project.storemanager_api.domain.menu.dto.response;

import com.project.storemanager_api.domain.ui.response.UiResponseDto;
import lombok.*;

@Getter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MenuResponseDto {

    private Long menuId;
    private Long storeId;
    private String menuName;
    private int discountRate; // 할인률
    private int price;
    private UiResponseDto menuStyle;

}
