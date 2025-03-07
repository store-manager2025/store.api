package com.project.storemanager_api.repository;

import com.project.storemanager_api.domain.pay.entity.PaymentType;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

;

@Mapper
@Repository
public interface PaymentDetailRepository {

    // 결제 상세 정보 저장
    void savePayInfo(
            @Param("paidMoney") Integer paidMoney,
            @Param("paymentType") PaymentType paymentType,
            @Param("paymentId") Long paymentId
    );

}
