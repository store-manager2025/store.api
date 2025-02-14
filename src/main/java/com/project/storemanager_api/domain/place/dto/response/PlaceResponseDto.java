package com.project.storemanager_api.domain.place.dto.response;

import lombok.*;

@Getter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PlaceResponseDto {

    private Long placeId;

    @Setter
    private Long uiId;

    private String placeName;

    @Setter
    private String sizeType;
}
