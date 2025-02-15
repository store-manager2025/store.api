package com.project.storemanager_api.domain.menu.dto.response;

import lombok.*;

@Getter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MenuDetailResponseDto {
    private Long menuId;
    private String menuName;
    private Integer discountRate;
    private Integer price; // 메뉴 가격
}
