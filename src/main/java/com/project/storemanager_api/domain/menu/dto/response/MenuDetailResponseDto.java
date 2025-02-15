package com.project.storemanager_api.domain.menu.dto.response;

import lombok.*;

@Getter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MenuDetailResponseDto {
    private String menuName;
    private Integer discountRate;
    private Integer totalPrice; // 메뉴 가격
    private Integer totalCount; // 메뉴 주문 횟수
}
