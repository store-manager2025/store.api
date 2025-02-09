package com.project.storemanager_api.domain.ui.dto.response;

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

}
