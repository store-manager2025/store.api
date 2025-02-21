package com.project.storemanager_api.domain.store.dto.request;

import lombok.*;

@Getter
@Setter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SaveStoreRequestDto {

    private Long storeId; // DB에서 auto-generated된 store_id를 받을 필드
    private Long userId;
    private String password;
    private String phoneNumber;
    private String storeName;
    private String storePlace;
}
