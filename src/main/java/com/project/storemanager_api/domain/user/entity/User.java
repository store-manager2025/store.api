package com.project.storemanager_api.domain.user.entity;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {

    private Long userId;
    private String password;
    private String email;
    private String name;
    private String role;
    private String refreshToken;
    private LocalDateTime createdAt;

}
