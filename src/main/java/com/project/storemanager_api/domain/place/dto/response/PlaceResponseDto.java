package com.project.storemanager_api.domain.place.dto.response;

import lombok.*;

@Getter
@Setter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PlaceResponseDto {

    private Long placeId;
    private Long uiId;
    private String placeName;

    // ui정보
    private String sizeType;
    private Integer positionX;
    private Integer positionY;
}
