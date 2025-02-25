package com.project.storemanager_api.repository;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import static com.project.storemanager_api.domain.pay.entity.PaymentDetail.PaymentType;

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
