package com.project.storemanager_api.domain.user.dto.request;

import lombok.*;

@Getter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ModifyUserDto {

    @Setter
    // token 검증 후 받는 데이터라 payload에 담을필요 x
    private String email;

    private String name;

    private String password;

}
