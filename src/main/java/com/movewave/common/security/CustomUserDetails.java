package com.movewave.common.security;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collection;
import java.util.Map;

/**
 * Spring Security의 UserDetails와 OAuth2User를 구현한 커스텀 사용자 상세 정보 클래스입니다.
 * 일반 로그인과 OAuth2 로그인 모두를 지원합니다.
 */
@AllArgsConstructor
public class CustomUserDetails implements UserDetails, OAuth2User {
    
    @Getter
    private final Long accountId; // 계정 ID
    
    private final String username; // 사용자 식별자 (로그인 ID)
    
    private final Collection<? extends GrantedAuthority> authorities; // 사용자 권한 목록
    
    private final transient Map<String, Object> attributes; // OAuth2 사용자 속성 (직렬화 제외)

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return null; // OAuth2 로그인에서는 비밀번호가 필요하지 않음
    }

    @Override
    public String getUsername() {
        return username;
    }

    // 계정 만료 여부
    @Override
    public boolean isAccountNonExpired() {
        return true; // 계정 만료되지 않음
    }

    // 계정 잠금 여부 
    @Override
    public boolean isAccountNonLocked() {
        return true; // 계정 잠기지 않음
    }

    // 자격 증명 만료 여부
    @Override
    public boolean isCredentialsNonExpired() {
        return true; // 자격 증명 만료되지 않음
    }

    // 계정 활성화 여부
    @Override
    public boolean isEnabled() {
        return true; // 계정 활성화됨
    }

    @Override
    public Map<String, Object> getAttributes() {
        return attributes;
    }

    @Override
    public String getName() {
        return username; // OAuth2User의 이름으로 loginId를 반환
    }
}
