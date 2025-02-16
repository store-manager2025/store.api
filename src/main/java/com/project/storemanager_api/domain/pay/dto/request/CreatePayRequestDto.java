package com.project.storemanager_api.domain.pay.dto.request;

import lombok.*;

import java.util.List;

import static com.project.storemanager_api.domain.pay.entity.Payment.PaymentType;

@Getter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreatePayRequestDto {

    private Long orderId;
    private String placeName;
    private Integer totalAmount;
    private Integer discountAmount;
    private List<PayRequestDto> payList;


    @Getter
    @ToString
    @EqualsAndHashCode
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    private static class PayRequestDto {
        private Integer paidMoney;
        private PaymentType paymentType;
    }

}
