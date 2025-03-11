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
public class Employees {

    private Long empId;
    private Long storeId;
    private String password;
    private String profileImg;
    private String name;
    private LocalDateTime createdAt;
}
