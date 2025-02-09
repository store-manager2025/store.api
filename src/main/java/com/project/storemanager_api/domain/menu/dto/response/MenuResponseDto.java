package com.project.storemanager_api.domain.menu.dto.response;

import com.project.storemanager_api.domain.menu.dto.request.SaveMenuRequestDto;
import com.project.storemanager_api.domain.ui.entity.UiLayout;
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


    public static MenuResponseDto toResponseDto(
            Long generatedMenuId,
            Long generatedUiId,
            SaveMenuRequestDto dto,
            UiLayout newUi) {

        return MenuResponseDto.builder()
                .menuId(generatedMenuId)
                .storeId(dto.getStoreId())
                .menuName(dto.getMenuName())
                .price(dto.getPrice())
                .discountRate(0)
                .menuStyle(UiResponseDto.builder()
                        .uiId(generatedUiId)
                        .colorCode(newUi.getColorCode())
                        .positionX(0)
                        .positionY(0)
                        .build())
                .build();

    }
}
