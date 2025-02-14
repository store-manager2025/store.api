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
public class Order {

    private Long orderId;
    private Long storeId;
    private Integer price;
    private OrderType orderType;
    private OrderStatus orderStatus;
    private LocalDateTime orderedAt;

    private enum OrderType {
        CASH, CARD, UNPAID
    }

    private enum OrderStatus {
        SUCCESS, UNPAID
    }
}
