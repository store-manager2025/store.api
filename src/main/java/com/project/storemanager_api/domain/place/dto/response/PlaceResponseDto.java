package com.project.storemanager_api.domain.place.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.project.storemanager_api.domain.ui.dto.response.UiResponseDto;
import lombok.*;

@Getter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PlaceResponseDto {

    private Long placeId;

    @JsonIgnore
    @Setter
    private Long uiId;

    private String placeName;

    @Setter
    private UiResponseDto style;
}
