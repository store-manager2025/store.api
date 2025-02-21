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
public class Receipt {

    private Long receiptId;
    private Long paymentId;
    private Long cardId;
    private String receiptNumber; // 영수증 번호
    private String approveNumber; // 승인번호 (8글자의 랜덤숫자)
    private String joinNumber; // 가맹번호 (9글자의 랜덤숫자)
    private LocalDateTime receiptDate;

}
