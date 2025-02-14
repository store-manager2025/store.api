package com.project.storemanager_api.domain.place.entity;

import lombok.*;

@Getter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SavePlaceRequestDto {

    @Setter
    private Long uiId;
    private Long storeId;
    private String placeName;
}
