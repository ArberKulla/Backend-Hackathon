package com.elinikon.merrbio.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public enum UserRole {
    BUYER("BUYER"),
    SELLER("SELLER"),
    ADMIN("ADMIN")
    ;
    @Getter
    private final String role;

    public List<SimpleGrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_" + this.name()));
    }

}
