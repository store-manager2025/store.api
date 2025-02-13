package com.project.storemanager_api.domain.menu.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.project.storemanager_api.domain.ui.dto.response.UiResponseDto;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MenuResponseDto {

    private Long menuId;
    private Long categoryId;
    private String menuName;
    private int discountRate; // 할인률
    private int price;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime createdAt;

    @JsonIgnore
    private Long uiId;

    @Setter
    private UiResponseDto menuStyle;

}
