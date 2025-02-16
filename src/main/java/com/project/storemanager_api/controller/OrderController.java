package com.project.storemanager_api.controller;

import com.project.storemanager_api.domain.order.dto.request.OrderRequestDto;
import com.project.storemanager_api.domain.order.dto.response.OrderAllResponseDto;
import com.project.storemanager_api.domain.order.dto.response.OrderDetailResponseDto;
import com.project.storemanager_api.service.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@Slf4j
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    // 주문 최초 생성
    @PostMapping
    public ResponseEntity<Map<String, Object>> createOrder(@RequestBody OrderRequestDto dto) {
        log.info("주문 생성 요청: {}", dto);
        orderService.createOrder(dto);
        return ResponseEntity.ok(Map.of("message", "주문이 성공적으로 생성되었습니다."));
    }

    // 추가 주문
    @PostMapping("/add/{orderId}")
    public ResponseEntity<Map<String, Object>> addOrder(@RequestBody OrderRequestDto dto, @PathVariable Long orderId) {
        log.info("주문 누적 요청: {}", dto);
        orderService.addOrder(dto, orderId);
        return ResponseEntity.ok(Map.of("message", "주문추가가 완료되었습니다."));
    }


    // 오더 단일 상세조회
    @GetMapping("/detail/{orderId}")
    public ResponseEntity<OrderDetailResponseDto> getOrder(@PathVariable Long orderId) {
        log.info("오더 단일 상세 조회 : {} ", orderId);
        OrderDetailResponseDto result = orderService.getDetail(orderId);
        return ResponseEntity.ok().body(result);
    }

    // 모든 오더 기록을 조회하는 API
    @GetMapping("/all/{storeId}")
    public ResponseEntity<List<OrderAllResponseDto>> getAllOrders(@PathVariable Long storeId) {
        log.info("모든 오더 기록 조회 요청 - {}", storeId);
        List<OrderAllResponseDto> result = orderService.getAllOrders(storeId);
        return ResponseEntity.ok().body(result);
    }

    // 특정 기간에 대한 주문 목록 조회
    @GetMapping
    public ResponseEntity<List<OrderAllResponseDto>> getPeriodOrderList(
            @RequestParam Long storeId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        log.info("시작일 : {} , 종료일 : {}", startDate, endDate);
        List<OrderAllResponseDto> result = orderService.getPeriodOrderList(storeId, startDate, endDate);
        return ResponseEntity.ok().body(result);
    }

    // 하루에 대한 주문 리스트
    @GetMapping("/daily")
    public ResponseEntity<List<OrderDetailResponseDto>> getDailyOrderList(
            @RequestParam Long storeId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
            List<OrderDetailResponseDto> result = orderService.getDailyOrderList(storeId, date);
            return ResponseEntity.ok().body(result);
    }
}
