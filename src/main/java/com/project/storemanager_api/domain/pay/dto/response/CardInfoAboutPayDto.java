package com.project.storemanager_api.domain.pay.dto.response;

import lombok.*;

@Getter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CardInfoAboutPayDto {
    private String cardCompany; // cards
    private String cardNumber; // cards
    private String inputMethod; // 입력방법. 일단 Swiped 기본값
    private String approveDate; // 승인일자. 12자리의 임의 숫자,
    private String approveNumber; // 승인번호. 8자리 임의 숫자
    private String installmentPeriod; // 할부기간. 일단 일시불 기본값
    private Integer paidMoney;
}
