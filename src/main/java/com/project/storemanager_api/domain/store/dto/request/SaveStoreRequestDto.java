package com.project.storemanager_api.domain.store.dto.request;

import lombok.*;

@Getter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SaveStoreRequestDto {

    @Setter
    private Long userId;
    @Setter
    private String password;

    private String storeName;
    private String storePlace;
}
