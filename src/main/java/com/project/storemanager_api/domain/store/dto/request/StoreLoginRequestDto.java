package com.project.storemanager_api.domain.store.dto.request;

import lombok.*;

@Getter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StoreLoginRequestDto {

    private Long storeId;
    private String password;

}
