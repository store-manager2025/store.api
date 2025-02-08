package com.project.storemanager_api.domain.dto.request;

import com.project.storemanager_api.domain.user.entity.User;
import lombok.*;

import javax.validation.constraints.Pattern;

@Getter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SignUpRequestDto {

    private String email;

    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d@$!%*#?&]{8,}$",
            message = "비밀번호는 8자 이상, 영문과 숫자 조합이어야 합니다.")
    private String password;

    @Pattern(regexp = "^[a-zA-Z0-9._가-힣]{3,20}$",
            message = "사용자 이름은 3-20자의 영문, 한글, 숫자, 밑줄, 마침표만 사용 가능합니다.")
    private String name;


    public User toEntity() {
        return User.builder()
                .email(email)
                .name(this.name)
                .password(this.password)
                .build();
    }
}
