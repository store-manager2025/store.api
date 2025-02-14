package com.project.storemanager_api.service;

import com.project.storemanager_api.domain.menu.dto.response.MenuResponseDto;
import com.project.storemanager_api.domain.order.dto.request.CreateOrderRequestDto;
import com.project.storemanager_api.domain.order.dto.request.OrderItemRequestDto;
import com.project.storemanager_api.domain.order.entity.Order;
import com.project.storemanager_api.exception.ErrorCode;
import com.project.storemanager_api.exception.MenuException;
import com.project.storemanager_api.repository.MenuRepository;
import com.project.storemanager_api.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@Slf4j
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderMenuService orderMenuService;
    private final MenuRepository menuRepository; // 메뉴 가격 조회를 위한 Repository

    public void createOrder(CreateOrderRequestDto dto) {
        // 1. 각 주문 항목의 가격 계산 및 총 주문 금액 합산
        int totalPrice = 0;
        for (OrderItemRequestDto item : dto.getItems()) {
            // 메뉴 정보를 조회하여 가격 가져오기
            MenuResponseDto menu = menuRepository.findById(item.getMenuId())
                    .orElseThrow(() -> new MenuException(ErrorCode.INVALID_ID, "메뉴를 찾을 수 없습니다. 메뉴 ID: " + item.getMenuId()));
            int menuPrice = menu.getPrice(); // 예: menuId 10 → 3500, menuId 12 → 3000
            log.info("메뉴 ID: {}, 가격: {}", menu.getMenuId(), menuPrice);

            // 주문 항목의 가격 = 메뉴 가격 * 주문 수량
            int itemTotalPrice = menuPrice * item.getQuantity();
            // 각 주문 항목에 계산된 가격을 세팅 (OrderItemRequestDto에 orderPrice 필드가 있음)
            item.setOrderPrice(itemTotalPrice);
            log.info("메뉴 ID: {}, 수량: {}, 계산된 주문 금액: {}", item.getMenuId(), item.getQuantity(), itemTotalPrice);
            totalPrice += itemTotalPrice;
        }
        log.info("계산된 총 주문 금액: {}", totalPrice);

        // 2. Order 엔티티 생성 (총 주문 금액 포함)
        Order order = Order.builder()
                .storeId(dto.getStoreId())
                .price(totalPrice)
                .placeId(dto.getPlaceId())
                .orderType(Order.OrderType.UNPAID)   // 기본값
                .orderStatus(Order.OrderStatus.UNPAID) // 기본값
                .build();

        // 3. orders 테이블에 주문 저장 (INSERT 후 auto-generated key 반영)
        orderRepository.saveOrder(order);
        log.info("주문 저장 완료, 생성된 orderId: {}", order.getOrderId());

        // 4. 각 주문 항목 저장: OrderMenuService를 통해 처리 (order_menu 테이블에 저장)
        for (OrderItemRequestDto item : dto.getItems()) {
            orderMenuService.createOrderMenu(order.getOrderId(), item);
        }
    }
}