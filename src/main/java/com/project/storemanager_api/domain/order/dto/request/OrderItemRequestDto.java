package com.project.storemanager_api.domain.order.dto.request;

import lombok.*;

@Getter
@Setter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderItemRequestDto {
    private Long orderMenuId;   // 자동 생성된 order_menu_id 값을 저장할 필드
    private Long menuId;        // 메뉴 식별자
    private Integer quantity;   // 주문 수량
    private Integer orderPrice; // 주문 항목 총액 (메뉴 가격 * 수량)
}