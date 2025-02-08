package com.project.storemanager_api.domain.user.dto.request;

import lombok.*;

import javax.validation.constraints.NotBlank;

@Getter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoginRequestDto {

    @NotBlank(message = "이메일은 필수입니다.")
    private String username;

    @NotBlank(message = "비밀번호는 필수입니다.")
    private String password;
}
