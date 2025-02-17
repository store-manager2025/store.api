package com.project.storemanager_api.repository;

import com.project.storemanager_api.domain.pay.dto.request.CreatePayRequestDto;
import com.project.storemanager_api.domain.pay.dto.response.PaymentResponseDto;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface PaymentRepository {

    // 결제 저장
    void savePayment(CreatePayRequestDto dto);

    List<PaymentResponseDto> findAllByStoreId(Long storeId);
}
