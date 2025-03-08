package com.project.storemanager_api.service;

import com.project.storemanager_api.domain.pay.dto.response.ReceiptResponseDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
@Rollback
class ReceiptServiceTest {

    @Autowired
    ReceiptService receiptService;


    @Test
    @DisplayName("payment id로 영수증 출력")
    void getReceiptDtoByPaymentId() {
        Long paymentId = 14L;
        ReceiptResponseDto receiptResponseDto = receiptService.printReceipt(paymentId);
        System.out.println("receiptResponseDto = " + receiptResponseDto);
    }


}