package com.project.storemanager_api.domain.store.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Builder
// 지금은 StoreResponseDto와 동일한 구조지만, 단일 조회라 JOIN이 들어갈 예정
public class StoreDetailResponseDto {

    private Long storeId;
    private String storeName;
    private String storePlace;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime createdAt;

}
