package com.project.storemanager_api.domain.dto.request;

import lombok.*;

@Getter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ModifyUserDto {

    @Setter
    private String email;

    private String name;

    private String password;

}
