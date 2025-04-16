package com.movewave.common.security.constants;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CookieConstants {
    // 토큰 생성을 위한 쿠키 설정
    public static final String COOKIE_ACCESS_TOKEN = "accessToken=%s; HttpOnly; Secure; Path=/; Max-Age=3600; SameSite=None";
    public static final String COOKIE_REFRESH_TOKEN = "refreshToken=%s; HttpOnly; Secure; Path=/; Max-Age=86400; SameSite=None";
    
    // 토큰 삭제를 위한 쿠키 설정 (Max-Age=0으로 설정하여 즉시 만료)
    public static final String COOKIE_DELETE_ACCESS_TOKEN = "accessToken=; HttpOnly; Secure; Path=/; Max-Age=0; SameSite=None";
    public static final String COOKIE_DELETE_REFRESH_TOKEN = "refreshToken=; HttpOnly; Secure; Path=/; Max-Age=0; SameSite=None";
}
