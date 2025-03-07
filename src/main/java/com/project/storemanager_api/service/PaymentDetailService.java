package com.project.storemanager_api.service;

import com.project.storemanager_api.domain.pay.entity.PaymentType;
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
    public void savePayInfo(Integer paidMoney, PaymentType paymentType, Long paymentId) {
        paymentDetailRepository.savePayInfo(paidMoney, paymentType, paymentId);
    }
}
