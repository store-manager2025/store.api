package com.project.storemanager_api.domain.report.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Getter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PeakTimeGroupedResponseDto {
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate date;  // 날짜 필드
    private List<PeakTimeDetailDto> detail;  // 시간대별 매출 리스트
}