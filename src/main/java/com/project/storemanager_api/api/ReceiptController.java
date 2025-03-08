package com.project.storemanager_api.api;

import com.project.storemanager_api.domain.pay.dto.response.ReceiptResponseDto;
import com.project.storemanager_api.service.ReceiptService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/receipts")
public class ReceiptController {

    private final ReceiptService receiptService;

    @GetMapping("/{orderId}")
    public ResponseEntity<ReceiptResponseDto> getReceipt(@PathVariable Long orderId) {
        ReceiptResponseDto receiptResponseDto = receiptService.printReceipt(orderId);
        return ResponseEntity.ok().body(receiptResponseDto);
    }

}
