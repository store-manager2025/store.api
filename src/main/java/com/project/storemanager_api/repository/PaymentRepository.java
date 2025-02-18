package com.project.storemanager_api.repository;

import com.project.storemanager_api.domain.pay.dto.request.CreatePayRequestDto;
import com.project.storemanager_api.domain.pay.dto.response.PaymentDetailResponseDto;
import com.project.storemanager_api.domain.pay.dto.response.PaymentResponseDto;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Mapper
@Repository
public interface PaymentRepository {

    // 결제 저장
    void savePayment(CreatePayRequestDto dto);

    // 한 매장에 대한 결제정보 전체 조회
    List<PaymentResponseDto> findAllByStoreId(Long storeId);

    // 결제 정보 단일 조회
    Optional<PaymentDetailResponseDto> findPaymentDetail(Long paymentId);
}
