package com.project.storemanager_api.service;

import com.project.storemanager_api.domain.pay.dto.request.CreatePaymentDetailDto;
import com.project.storemanager_api.repository.PaymentDetailRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
@Service
@Transactional
public class PaymentDetailService {

    private final PaymentDetailRepository paymentDetailRepository;


    // 결제 세부 정보 저장
    public void savePayInfo(CreatePaymentDetailDto detailDto, Long paymentId) {
        paymentDetailRepository.savePayInfo(detailDto.getPaidMoney(), detailDto.getPaymentType(), paymentId);
    }
}
