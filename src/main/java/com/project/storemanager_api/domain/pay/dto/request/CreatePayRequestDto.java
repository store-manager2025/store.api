package com.project.storemanager_api.domain.pay.dto.request;

import com.project.storemanager_api.domain.pay.entity.PaymentType;
import com.project.storemanager_api.domain.pay.entity.Status;
import lombok.*;

@Getter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Builder
// 결제 요청 한건에 대한 dto
public class CreatePayRequestDto {

    @Setter
    private Long paymentId;
    @Setter
    private Long storeId;
    @Setter
    private Status status;

    private Long orderId;
    private Long placeId;
//    private Integer totalAmount;
    private Integer discountAmount;
    private Integer paidMoney;
    private PaymentType paymentType;

    // 카드 정보
    private String cardCompany;
    private String cardNumber;
    private String expiryDate;

}
