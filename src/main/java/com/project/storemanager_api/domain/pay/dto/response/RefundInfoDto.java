package com.project.storemanager_api.domain.pay.dto.response;

import lombok.*;

import static com.project.storemanager_api.domain.pay.entity.Payment.Status;

@Getter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Builder
// 환불 요청이 들어왔을때, 조회용 기존 데이터를 담아올 Dto
public class RefundInfoDto {

    private Status status;
    private Integer paymentAmount;
    private Long orderId;

}
