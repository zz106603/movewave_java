package com.movewave.user.controller;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * API URL 상수를 관리하는 유틸리티 클래스입니다.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class AccountApiUrls {

    /**
     * 사용자 정보 API 엔드포인트
     */
    public static final String ACCOUNT_URL = "/api/account";

    /**
     * 로그아웃 API 엔드포인트
     */
    public static final String LOGOUT_URL = "/api/account/logout";
}
