package com.project.storemanager_api.domain.order.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.time.LocalDateTime;

import static com.project.storemanager_api.domain.order.entity.Order.OrderStatus;
import static com.project.storemanager_api.domain.order.entity.Order.OrderType;

@Getter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderResponseDto {

    private Long orderId; // orderMenu와 조인도 해야함
    private OrderType orderType;
    private OrderStatus orderStatus;
    private Integer price;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime orderedAt;

    // 메뉴테이블과 조인


}
