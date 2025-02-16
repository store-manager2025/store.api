package com.project.storemanager_api.domain.pay.dto.request;

import lombok.*;

import static com.project.storemanager_api.domain.pay.entity.PaymentDetail.PaymentType;

@Getter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreatePaymentDetailDto {

    private Integer paidMoney;
    private PaymentType paymentType;

}
