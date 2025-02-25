package com.project.storemanager_api.domain.report.dto.response;

import lombok.*;

@Getter
@Setter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SalesByPaymentType {

    private String type;
    private Integer amount;
    private String ratio;

}
