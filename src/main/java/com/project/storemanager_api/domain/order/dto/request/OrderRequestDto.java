package com.project.storemanager_api.domain.order.dto.request;

import lombok.*;

import java.util.List;

@Getter
@Setter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderRequestDto {
    private Long storeId;                     // 주문이 발생한 매장 ID
    private Long placeId;                     // 주문 장소 ID
    private Integer totalPrice;               // 주문 전체 금액
    private List<OrderItemRequestDto> items;  // 주문 항목 리스트

}