package com.project.storemanager_api.domain.dto.request;

import lombok.*;

@Getter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ModifyUserDto {

    private Long userId;

    private String name;

    private String password;

}
