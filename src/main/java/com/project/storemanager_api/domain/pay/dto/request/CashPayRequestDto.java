package com.project.storemanager_api.domain.pay.dto.request;

import com.project.storemanager_api.domain.pay.entity.PaymentType;
import lombok.*;

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
