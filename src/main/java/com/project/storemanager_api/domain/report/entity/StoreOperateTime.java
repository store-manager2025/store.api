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
public class StoreOperateTime {

    private Long storeOpTimeID;
    private Long storeId;
    private LocalDateTime openedAt;
    private LocalDateTime closedAt;
    private LocalDateTime date;

}
