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
public class Payment {

    private Long paymentId;
    private Long orderId; // 주문과 연결 1:1관계, NOT NULL
    private Integer paymentAmount; // 해당 결제 방식의 결제 금액 (총 금액), NOT NULL
    private Integer discountAmount; // 할인 결제인 경우
    private Status status;
    private LocalDateTime createdAt;

    public enum Status {
        PENDING,
        SUCCESS
    }

}
