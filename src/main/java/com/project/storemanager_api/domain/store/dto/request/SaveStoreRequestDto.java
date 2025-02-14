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
    private Long storeId; // DB에서 auto-generated된 store_id를 받을 필드
    @Setter
    private Long userId;
    @Setter
    private String password;

    private String storeName;
    private String storePlace;
}
