package com.project.storemanager_api.domain.pay.entity;

import lombok.*;

@Getter
@Setter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PayTransaction {

    private Long transactionId;
    private Long paymentId;
    private Long cardId;
    private Integer transactionAmount; // 거래 금액
    private TransactionStatus transactionStatus;
    private Long createdAt;

    public enum TransactionStatus {
        APPROVE, // 승인됨
        DECLINED, // 거절됨
        PENDING // 대기중 (기본값)
    }
}
