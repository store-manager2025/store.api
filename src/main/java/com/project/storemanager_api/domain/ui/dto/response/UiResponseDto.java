package com.project.storemanager_api.domain.ui.dto.response;

import com.project.storemanager_api.domain.ui.entity.UiLayout;
import lombok.*;

@Getter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UiResponseDto {

    private Long uiId;
    private Integer positionX;
    private Integer positionY;
    private String colorCode;
    private String sizeType;

    public static UiResponseDto toResponseDto(Long uiId, UiLayout ui) {
        return UiResponseDto.builder()
                .uiId(uiId)
                .colorCode(ui.getColorCode())
                .positionX(ui.getPositionX())
                .positionY(ui.getPositionY())
                .sizeType(ui.getSizeType())
                .build();
    }
}
