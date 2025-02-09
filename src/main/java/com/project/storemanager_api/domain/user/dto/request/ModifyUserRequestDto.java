package com.project.storemanager_api.domain.user.dto.request;

import lombok.*;

@Getter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ModifyUserRequestDto {

    private String name;

    private String password;

}
