package com.project.storemanager_api.domain.pay.dto.request;

import lombok.*;

import static com.project.storemanager_api.domain.pay.entity.PaymentDetail.PaymentType;

@Getter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CashPayRequestDto {

    private Long orderId;
    private Long placeId;
    private Integer paidMoney;
    private PaymentType paymentType;

}
