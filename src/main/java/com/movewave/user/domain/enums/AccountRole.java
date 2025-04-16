package com.movewave.user.domain.enums;

/**
 * 계정의 권한 역할을 정의하는 열거형입니다.
 * Spring Security에서 사용되는 권한 체계를 따릅니다.
 */
public enum AccountRole {
    /**
     * 일반 사용자 권한
     */
    ROLE_USER,

    /**
     * 관리자 권한
     */
    ROLE_ADMIN,

    /**
     * 매니저 권한
     */
    ROLE_MANAGER
}
