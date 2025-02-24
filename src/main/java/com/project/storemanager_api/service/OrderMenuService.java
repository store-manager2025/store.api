package com.project.storemanager_api.service;

import com.project.storemanager_api.domain.order.dto.request.OrderItemRequestDto;
import com.project.storemanager_api.domain.order.dto.request.RefundOrderDto;
import com.project.storemanager_api.domain.order.entity.OrderMenu;
import com.project.storemanager_api.exception.ErrorCode;
import com.project.storemanager_api.exception.OrderException;
import com.project.storemanager_api.repository.OrderMenuRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
@Slf4j
@RequiredArgsConstructor
public class OrderMenuService {

    private final OrderMenuRepository orderMenuRepository;

    /**
     * 추가 주문 시, 이미 주문한 메뉴가 있다면 수량 및 가격 업데이트, 없으면 신규 저장
     *
     * @param orderId 주문 ID
     * @param item    주문 항목 DTO
     */
    public void createOrderMenu(Long orderId, OrderItemRequestDto item) {
        // 동일한 주문 내 동일한 메뉴가 이미 존재하는지 조회
        Optional<OrderMenu> existing = orderMenuRepository.findByOrderIdAndMenuId(orderId, item.getMenuId());

        if (existing.isPresent()) {
            // 기존 항목이 있다면, 수량과 가격을 업데이트
            orderMenuRepository.updateQuantityAndPrice(orderId, item.getMenuId(), item.getQuantity(), item.getOrderPrice());
            log.info("기존 주문 항목 업데이트: orderId={}, menuId={}, 추가 수량={}, 추가 가격={}",
                    orderId, item.getMenuId(), item.getQuantity(), item.getOrderPrice());
        } else {
            // 없으면 새로운 주문 항목 삽입
            orderMenuRepository.saveOrderMenu(orderId, item);
            log.info("신규 주문 항목 저장: orderId={}, item={}", orderId, item);
        }
    }

    public List<RefundOrderDto> findOriginOrderMenus(Long orderId) {
        return orderMenuRepository.findOriginMenus(orderId);
    }

    // 주문 메뉴 상태를 취소로 변경 (완전 취소인 경우)
    public void updateOrderStatus(Long orderId, Long menuId, String orderStatus) {
        orderMenuRepository.updateStatus(orderId, menuId, orderStatus);
    }

    // 주문 메뉴 수량만 감소 (부분 취소인 경우)
    public void updateMenuQuantity(Long orderId, Long menuId, int quantity) {
        // 메뉴 원가 구해와서 연산 후 집어넣어야 함
        OrderMenu originOrderInfo = orderMenuRepository.findByOrderIdAndMenuId(orderId, menuId).orElseThrow(
                () -> new OrderException(ErrorCode.ORDER_NOT_FOUND, ErrorCode.ORDER_NOT_FOUND.getMessage())
        );
        // 원래 주문된 가격 - 1개당 원가 * quantity
        Integer currentOrderAmount = originOrderInfo.getOrderPrice();
        Integer orderPrice = currentOrderAmount - (currentOrderAmount / originOrderInfo.getOrderItemQuantity()) * quantity;

        orderMenuRepository.updateOrderMenuQuantity(orderId, menuId, quantity, orderPrice);
    }

    public void updateOrderStatusWithoutMenu(Long orderId, String orderStatus) {
        orderMenuRepository.updateOrderStatusWithoutMenuId(orderId, orderStatus);
    }
}