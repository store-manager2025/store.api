package com.project.storemanager_api.service;

import com.project.storemanager_api.domain.order.dto.request.OrderItemRequestDto;
import com.project.storemanager_api.repository.OrderMenuRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@Slf4j
@RequiredArgsConstructor
public class OrderMenuService {

    private final OrderMenuRepository orderMenuRepository;

    /**
     * 주문 항목을 저장하는 메서드
     * @param orderId 주문 ID
     * @param item    주문 항목 DTO
     */
    public void createOrderMenu(Long orderId, OrderItemRequestDto item) {
        orderMenuRepository.saveOrderMenu(orderId, item);
        log.info("주문 항목 저장: orderId={}, item={}", orderId, item);
    }
}
