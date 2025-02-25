package com.project.storemanager_api.domain.report.dto.response;

import lombok.*;

import java.util.List;

@Getter
@Setter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SalesByPaymentTypeResponseDto {

    private Integer totalAmount;
    private List<SalesByPaymentType> typeAndDetail;

}
