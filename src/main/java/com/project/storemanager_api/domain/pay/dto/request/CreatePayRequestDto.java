package com.project.storemanager_api.domain.pay.dto.request;

import lombok.*;

import java.util.List;

@Getter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreatePayRequestDto {

    @Setter
    private Long paymentId;

    private Long orderId;
    private Long placeId;
    private Integer totalAmount;
    private Integer discountAmount;

    private List<CreatePaymentDetailDto> payList;

}
