package com.project.storemanager_api.domain.menu.dto.request;

import lombok.*;

@Getter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SaveMenuRequestDto {

    private Long storeId;

    @Setter
    private Long uiId;

    private String menuName;
    private Integer price;

}
