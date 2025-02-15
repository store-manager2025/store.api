package com.project.storemanager_api.domain.order.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.project.storemanager_api.domain.menu.dto.response.MenuDetailResponseDto;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

import static com.project.storemanager_api.domain.order.entity.Order.OrderStatus;
import static com.project.storemanager_api.domain.order.entity.Order.OrderType;

@Getter
@Setter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderDetailResponseDto {

    private Long orderId;
    private Long storeId;
    private Integer price;
    private OrderType orderType;
    private OrderStatus orderStatus;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime orderedAt;
    private String placeName;

//    @JsonIgnore
    private List<Long> menuId;

    // 중첩 객체로 메뉴 상세 정보
    private List<MenuDetailResponseDto> menuDetail;
}