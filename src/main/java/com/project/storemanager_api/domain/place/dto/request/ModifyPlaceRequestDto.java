package com.project.storemanager_api.domain.place.dto.request;

import lombok.*;

@Getter
@Setter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ModifyPlaceRequestDto {

    private Long placeId;
    private Long uiId;

    // 수정 가능한 값들
    private String placeName;
    private String sizeType;
}
