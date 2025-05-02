package com.haruspeak.api.user.dto;

import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Getter
public class CustomUserPrincipal implements UserDetails {
    private final Integer userId;
    private final String name;

    // 기존 생성자, getter 유지
    public CustomUserPrincipal(final Integer userId, final String name) {
        this.userId = userId;
        this.name = name;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(); // 권한 없으면 빈 리스트
    }

    @Override
    public String getPassword() {
        return null; // 비밀번호 없음
    }

    @Override
    public String getUsername() {
        return String.valueOf(name);
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
