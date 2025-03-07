package com.project.storemanager_api.domain.pay.dto.request;

import com.project.storemanager_api.domain.pay.entity.PaymentType;
import lombok.*;

@Getter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreatePaymentDetailDto {

    private Integer paidMoney;
    private PaymentType paymentType;
    private String cardCompany;
    private String cardNumber;
    private String expiryDate;

}
