package com.project.storemanager_api.domain.user.dto.response;

import lombok.*;

@Getter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EmpResponseDto {

    private Long empId;
    private String profileImg;
    private String name;
    private String password;
}
