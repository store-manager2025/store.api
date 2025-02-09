package com.project.storemanager_api.domain.category.entity;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Category {

    private Long categoryId;
    private Long storeId;
    private Long uiId;
    private String categoryName;
    private LocalDateTime createdAt;

}
