package com.project.storemanager_api.domain.report.dto.response;

import lombok.*;

@Getter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Builder
// 매장 카테고리별 매출 분석
public class SalesByCategoryDto {

    @Setter
    private String ratio;
    private String categoryName;
    private Integer totalSales;
}
