package com.project.storemanager_api.api;

import com.project.storemanager_api.annotation.StoreAuthCheck;
import com.project.storemanager_api.domain.pay.dto.request.CreatePayRequestDto;
import com.project.storemanager_api.domain.pay.dto.response.PaymentDetailResponseDto;
import com.project.storemanager_api.domain.pay.dto.response.PaymentResponseDto;
import com.project.storemanager_api.domain.pay.dto.response.ReceiptResponseDto;
import com.project.storemanager_api.domain.user.dto.response.CustomUserPrincipal;
import com.project.storemanager_api.service.PaymentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/pay")
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping
    public ResponseEntity<ReceiptResponseDto> createPayment(@RequestBody CreatePayRequestDto dto) {
        log.info("Create payment request: {}", dto);
        ReceiptResponseDto receiptResponseDto = paymentService.requestPayment(dto);
        // 결제 성공시, 영수증 반환
        return ResponseEntity.ok().body(receiptResponseDto);
    }

    @GetMapping("/all/{storeId}")
    @StoreAuthCheck
    public ResponseEntity<List<PaymentResponseDto>> getAllPayments(@AuthenticationPrincipal CustomUserPrincipal userData,
                                                                   @PathVariable Long storeId) {
        List<PaymentResponseDto> allPayments = paymentService.getAllPayments(storeId);
        return ResponseEntity.ok().body(allPayments);
    }

    @GetMapping("/detail")
    public ResponseEntity<PaymentDetailResponseDto> getPaymentDetails(@RequestParam Long paymentId) {
        log.info("Get payment details request: {}", paymentId);
        PaymentDetailResponseDto result = paymentService.getPaymentDetail(paymentId);
        return ResponseEntity.ok().body(result);
    }

}
