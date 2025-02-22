package com.project.storemanager_api.domain.report.entity;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Report {

    private Long reportId;
    private Long storeId;
    private Integer dayOrderCount; // 해당 날짜의 총 주문 수
    private Integer dayAmount;
    private LocalDateTime openAt;
    private LocalDateTime closeAt;

}
