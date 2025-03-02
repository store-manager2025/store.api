package com.project.storemanager_api.domain.order.dto.request;

import lombok.*;

@Getter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderMenuCancelDto {

    private Long menuId;
    private Integer quantity;
}
