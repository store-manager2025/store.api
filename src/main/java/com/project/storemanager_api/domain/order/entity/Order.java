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
    private Long placeId;
    private Integer price;
    private OrderType orderType;
    private OrderStatus orderStatus;
    private LocalDateTime orderedAt;

    public enum OrderType {
        CASH, CARD, UNPAID
    }

    public enum OrderStatus {
        SUCCESS, UNPAID
    }
}
