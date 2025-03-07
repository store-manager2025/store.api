package com.project.storemanager_api.domain.pay.dto.request;

import lombok.*;

@Getter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CardPayRequestDto {

    private Long orderId;
    private Long placeId;
    private Integer paidMoney;
    private String paymentType;
    private String cardCompany;
    private String cardNumber;
    private String expiryDate;

}
