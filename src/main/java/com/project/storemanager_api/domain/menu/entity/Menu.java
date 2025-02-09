package com.project.storemanager_api.domain.menu.entity;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Menu {

    private Long menuId;
    private Long storeId;
    private Long uiId;
    private String menuName;
    private int discountRate; // 할인률
    private int price;
    private LocalDateTime createdAt;

}
