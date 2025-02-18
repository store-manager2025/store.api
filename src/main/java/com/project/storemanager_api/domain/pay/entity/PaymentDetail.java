package com.project.storemanager_api.domain.pay.entity;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaymentDetail {

    private Long paymentDetailId;
    private Long paymentId;
    private PaymentType paymentType;
    private Integer paidMoney;
    private LocalDateTime createdAt;


    public enum PaymentType {
        CASH // 현금
        , CARD // 카드
        , DIVIDE // 분할결제
        , MIX // 현금 + 카드
    }

}
