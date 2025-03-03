package com.project.storemanager_api.repository;

import com.project.storemanager_api.domain.pay.dto.request.CreatePayRequestDto;
import com.project.storemanager_api.domain.pay.dto.response.PaymentDetailResponseDto;
import com.project.storemanager_api.domain.pay.dto.response.PaymentResponseDto;
import com.project.storemanager_api.domain.pay.dto.response.RefundInfoDto;
import com.project.storemanager_api.domain.pay.entity.Payment;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
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

    // 결제 성공시 상태값 변경
    void changeStatus(@Param("status") Payment.Status status,
                      @Param("paymentId") Long paymentId);

    // paymentId 로 상태값 변경
    void updateStatusByPaymentId(@Param("paymentId") Long paymentId,
                               @Param("status") String status);

    // 환불 요청이 들어왔을 때, id에 해당하는 정보를 가져올 메서드
    Optional<RefundInfoDto> findRefundOriginDataByPaymentId(Long paymentId);

}
