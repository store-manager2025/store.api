package com.project.storemanager_api.domain.order.entity;

import lombok.*;

import java.time.LocalDateTime;

import static com.project.storemanager_api.domain.order.entity.Order.*;

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
    private Long cardId;
    private Integer orderItemQuantity; // 상품 주문 수량
    private Integer orderPrice; // 주문 상품의 개별 가격
    private OrderStatus orderStatus;
    private LocalDateTime createdAt;

}
