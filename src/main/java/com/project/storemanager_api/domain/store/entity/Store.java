package com.project.storemanager_api.domain.store.entity;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Store {

    private Long storeId;
    private Long userId;
    private String storeName;
    private String storePlace;
    private LocalDateTime createdAt;

}
