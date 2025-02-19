package com.project.storemanager_api.domain.user.dto.response;

import lombok.*;

import java.util.List;

@Getter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class CustomUserPrincipal {
    private Long userId;
    private List<Long> storeIds;

}