package com.project.storemanager_api.domain.order.dto.response;

import lombok.*;

@Getter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderMenuDetailResponseDto {

    private Long orderMenuId;
    private Integer orderItemQuantity;
    private Integer orderPrice;
}
