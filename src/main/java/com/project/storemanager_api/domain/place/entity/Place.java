package com.project.storemanager_api.domain.place.entity;

import lombok.*;

@Getter
@Setter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Place {

    private Long placeId;
    private Long uiId;
    private Long storeId;
    private String placeName;
}