package com.project.storemanager_api.service;

import com.project.storemanager_api.domain.order.entity.Order;
import com.project.storemanager_api.domain.pay.dto.request.CreatePayRequestDto;
import com.project.storemanager_api.domain.pay.dto.request.CreatePaymentDetailDto;
import com.project.storemanager_api.domain.pay.dto.response.PaymentResponseDto;
import com.project.storemanager_api.exception.*;
import com.project.storemanager_api.repository.OrderRepository;
import com.project.storemanager_api.repository.PaymentRepository;
import com.project.storemanager_api.repository.PlaceRepository;
import com.project.storemanager_api.repository.StoreRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final PaymentDetailService paymentDetailService;
    private final OrderRepository orderRepository;
    private final PlaceRepository placeRepository;
    private final StoreRepository storeRepository;


    public void requestPayment(CreatePayRequestDto dto) {
        log.info("requestPayment의 DTO : {} ", dto.toString());
        // 주문 id가 유효한지 검증
        Order foundOrder = validateOrderId(dto.getOrderId());
        // 유효하다면 storeId 저장
        dto.setStoreId(foundOrder.getStoreId());
        // place id가 유효한지 검증
        validatePlaceId(dto.getPlaceId());

        // 긁어야 하는 돈보다, 요청 들어온 돈이 큰 경우
        if (foundOrder.getPrice() < dto.getTotalAmount()) {
            throw new PaymentException(ErrorCode.TOO_MUCH_PRICE, "필요한 금액을 초과합니다.");
        }

        // 유효하다면 결제 정보 저장
        paymentRepository.savePayment(dto);

        // order쪽에서의 orderStatus도 SUCCESS로 변경
        orderRepository.updateOrderStatus(dto.getOrderId(), "SUCCESS");

        // 저장 후 생성된 id 받아와서 결제디테일 테이블에 저장
        Long generatedPaymentId = dto.getPaymentId();
        log.info("생성된 결제ID : {}", generatedPaymentId);

        for (CreatePaymentDetailDto payDetail : dto.getPayList()) {
            paymentDetailService.savePayInfo(payDetail, generatedPaymentId);
        }

    }

    // 한 매장에 등록된 결제 정보 반환 메서드
    public List<PaymentResponseDto> getAllPayments(Long storeId) {
        // 1. storeId가 유효한지 검증
        validateStoreId(storeId);

        // 2. db에 전달 후 데이터 받아옴
        List<PaymentResponseDto> result = paymentRepository.findAllByStoreId(storeId);
        return result;

    }

    private void validateStoreId(Long storeId) {
        storeRepository.findPasswordById(storeId).orElseThrow(
                () -> new StoreException(ErrorCode.STORE_NOT_FOUND, ErrorCode.STORE_NOT_FOUND.getMessage())
        );
    }


    private Order validateOrderId(Long orderId) {
        return orderRepository.findById(orderId).orElseThrow(
                () -> new OrderException(ErrorCode.INVALID_ID, "주문 내역을 찾을 수 없습니다.")
        );
    }

    private void validatePlaceId(Long placeId) {
        placeRepository.findById(placeId).orElseThrow(
                () -> new PlaceException(ErrorCode.INVALID_ID, "장소 정보를 찾을 수 없습니다.")
        );
    }

}
