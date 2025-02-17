package com.project.storemanager_api.controller;

import com.project.storemanager_api.domain.pay.dto.request.CreatePayRequestDto;
import com.project.storemanager_api.domain.pay.dto.response.PaymentResponseDto;
import com.project.storemanager_api.service.PaymentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/pay")
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping
    public ResponseEntity<Map<String, Object>> createPayment(@RequestBody CreatePayRequestDto dto) {
        log.info("Create payment request: {}", dto);
        paymentService.requestPayment(dto);
        return ResponseEntity.ok().body(Map.of("message", "결제가 완료되었습니다."));
    }

    @GetMapping("/all/{storeId}")
    public ResponseEntity<?> getAllPayments(@PathVariable Long storeId) {
        List<PaymentResponseDto> allPayments = paymentService.getAllPayments(storeId);
        return ResponseEntity.ok().body(allPayments);
    }




}
