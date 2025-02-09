package com.project.storemanager_api.domain.ui.dto.request;

import lombok.*;

@Getter
@Setter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChangeStyleRequestDto {

    private Long uiId;

    // 업데이트 될 필드들
    private Integer positionX;
    private Integer positionY;
    private String colorCode;
    private String sizeType;

}
