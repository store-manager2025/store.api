package com.project.storemanager_api.controller;

import com.project.storemanager_api.domain.order.dto.request.OrderRequestDto;
import com.project.storemanager_api.service.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@Slf4j
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    public ResponseEntity<?> createOrder(@RequestBody OrderRequestDto dto) {
        log.info("주문 생성 요청: {}", dto);
        orderService.createOrder(dto);
        return ResponseEntity.ok(Map.of("message", "주문이 성공적으로 생성되었습니다."));
    }

    @PostMapping("/add/{orderId}")
    public ResponseEntity<?> addOrder(@RequestBody OrderRequestDto dto) {
        return null;
    }
}
