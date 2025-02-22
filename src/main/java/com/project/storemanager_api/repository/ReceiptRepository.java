package com.project.storemanager_api.repository;

import com.project.storemanager_api.domain.pay.dto.response.ReceiptResponseDto;
import com.project.storemanager_api.domain.pay.entity.Receipt;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@Mapper
public interface ReceiptRepository {

    void saveReceipt(Receipt receipt);

    Optional<ReceiptResponseDto> findByOrderId(Long orderId);

}
