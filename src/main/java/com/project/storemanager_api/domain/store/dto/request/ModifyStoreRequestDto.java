package com.project.storemanager_api.domain.store.dto.request;

import lombok.*;

@Getter
@Setter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ModifyStoreRequestDto {

    private Long storeId;
    private String storeName;
    private String storePlace;
    private String password;
}
