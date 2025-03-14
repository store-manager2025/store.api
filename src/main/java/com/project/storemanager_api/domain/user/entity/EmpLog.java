package com.project.storemanager_api.domain.user.entity;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Builder
// 직원 근태 관리
public class EmpLog {

    private Long empLogId;
    private Long empId;
    private LocalDateTime workedAt;
    private LocalDateTime leavedAt;

}
