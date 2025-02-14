package com.project.storemanager_api.domain.order.entity;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderMenu {

    private Long orderMenuId;
    private Long orderId;
    private Long menuId;
    private Long placeId;
    private Integer orderItemQuantity; // 상품 주문 수량
    private Integer orderPrice; // 주문 상품의 개별 가격
    private LocalDateTime createdAt;
}
