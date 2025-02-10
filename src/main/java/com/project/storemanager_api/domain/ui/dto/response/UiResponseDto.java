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

    public static UiResponseDto toResponseDto(Long generatedUiId, UiLayout newUi) {
        return UiResponseDto.builder()
                .uiId(generatedUiId)
                .colorCode(newUi.getColorCode())
                .positionX(0)
                .positionY(0)
                .build();
    }
}
