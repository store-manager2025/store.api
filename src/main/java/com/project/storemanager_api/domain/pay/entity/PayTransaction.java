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
public class PayTransaction {
    private Long transactionId;
    private Long paymentId;
    private Long cardId;
    private Integer transactionAmount; // 거래 금액
    private TransactionStatus transactionStatus;
    private LocalDateTime createdAt;

    public enum TransactionStatus {
        APPROVED, // 승인됨
        DECLINED, // 거절됨
        PENDING // 대기중 (기본값)
    }
}
