package com.project.storemanager_api.domain.user.dto.request;

import lombok.*;

@Getter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ModifyUserDto {

    private String name;

    private String password;

}
