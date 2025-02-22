package com.project.storemanager_api.domain.user.dto.response;

import lombok.*;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collections;
import java.util.List;

@Getter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class CustomUserPrincipal {

    private Long userId;
    private List<Long> storeIds;
    private String role;

    public List<SimpleGrantedAuthority> getAuthorities() {
        return Collections.singletonList(new SimpleGrantedAuthority(this.role));
    }
}