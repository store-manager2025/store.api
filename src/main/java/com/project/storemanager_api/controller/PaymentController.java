package com.project.storemanager_api.controller;

import com.project.storemanager_api.domain.pay.dto.request.CreatePayRequestDto;
import com.project.storemanager_api.service.PaymentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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



}
