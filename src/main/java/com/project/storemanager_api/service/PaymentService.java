package com.project.storemanager_api.service;

import com.project.storemanager_api.domain.order.entity.Order;
import com.project.storemanager_api.domain.pay.dto.request.CreatePayRequestDto;
import com.project.storemanager_api.domain.pay.dto.request.CreatePaymentDetailDto;
import com.project.storemanager_api.exception.ErrorCode;
import com.project.storemanager_api.exception.OrderException;
import com.project.storemanager_api.repository.OrderRepository;
import com.project.storemanager_api.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final PaymentDetailService paymentDetailService;
    private final OrderRepository orderRepository;


    public void requestPayment(CreatePayRequestDto dto) {
        log.info("requestPayment의 DTO : {} ", dto.toString());
        // 주문 id가 유효한지 검증
        checkOrderId(dto.getOrderId());

        // 유효하다면 결제 정보 저장
        paymentRepository.savePayment(dto);

        // 저장 후 생성된 id 받아와서 결제디테일 테이블에 저장
        Long generatedPaymentId = dto.getPaymentId();
        log.info("생성된 결제ID : {}", generatedPaymentId);

        for (CreatePaymentDetailDto payDetail : dto.getPayList()) {
            paymentDetailService.savePayInfo(payDetail, generatedPaymentId);
        }

    }



    private Order checkOrderId(Long orderId) {
        return orderRepository.findById(orderId).orElseThrow(
                () -> new OrderException(ErrorCode.INVALID_ID, "주문 내역을 찾을 수 없습니다")
        );
    }
}
