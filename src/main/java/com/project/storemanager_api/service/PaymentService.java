package com.project.storemanager_api.service;

import com.project.storemanager_api.domain.order.entity.Order;
import com.project.storemanager_api.domain.pay.dto.request.CreatePayRequestDto;
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
    private final OrderRepository orderRepository;


    public void requestPayment(CreatePayRequestDto dto) {

        // 주문 id가 유효한지 검증
        checkOrderId(dto.getOrderId());

        paymentRepository.savePayment(dto);

    }



    private Order checkOrderId(Long orderId) {
        return orderRepository.findById(orderId).orElseThrow(
                () -> new OrderException(ErrorCode.INVALID_ID, "주문 내역을 찾을 수 없습니다")
        );
    }
}
