package com.project.storemanager_api.domain.report.dto.response;

import lombok.*;

@Getter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PeakTimeDetailDto {
    private String timeRange;  // 시간대 (예: "10:00-11:00")
    private Long amount;  // 해당 시간대의 금액
}