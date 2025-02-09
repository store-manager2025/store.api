package com.project.storemanager_api.domain.ui.entity;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UiLayout {

    private Long uiId;
    private Long storeId;
    private int positionX;
    private int positionY;
    private String colorCode;
    private String sizeType;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

}
