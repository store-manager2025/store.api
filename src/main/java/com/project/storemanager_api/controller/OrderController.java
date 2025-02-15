package com.project.storemanager_api.controller;

import com.project.storemanager_api.domain.order.dto.request.OrderRequestDto;
import com.project.storemanager_api.domain.order.dto.response.OrderDetailResponseDto;
import com.project.storemanager_api.service.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@Slf4j
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    // 주문 최초 생성
    @PostMapping
    public ResponseEntity<?> createOrder(@RequestBody OrderRequestDto dto) {
        log.info("주문 생성 요청: {}", dto);
        orderService.createOrder(dto);
        return ResponseEntity.ok(Map.of("message", "주문이 성공적으로 생성되었습니다."));
    }

    // 추가 주문
    @PostMapping("/add/{orderId}")
    public ResponseEntity<?> addOrder(@RequestBody OrderRequestDto dto, @PathVariable Long orderId) {
        log.info("주문 누적 요청: {}", dto);
        orderService.addOrder(dto, orderId);
        return ResponseEntity.ok(Map.of("message", "주문추가가 완료되었습니다."));
    }


    // 오더 단일 상세조회
    @GetMapping("/{orderId}")
    public ResponseEntity<?> getOrder(@PathVariable Long orderId) {
        log.info("오더 단일 상세 조회 : {} ", orderId);
        OrderDetailResponseDto result = orderService.getDetail(orderId);
        return ResponseEntity.ok().body(result);
    }





}
