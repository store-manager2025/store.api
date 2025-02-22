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
public class Card {

    private Long cardId;
    private Long paymentId;
    private String cardNumber;
    private String cardCompany;
    private Integer paidMoney;
    private LocalDateTime createdAt;
    private String expiryDate; // 2024/03
}
