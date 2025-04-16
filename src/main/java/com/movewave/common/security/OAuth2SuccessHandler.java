package com.movewave.common.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.movewave.common.properties.RedirectProperties;
import com.movewave.common.security.constants.CookieConstants;
import com.movewave.common.security.jwt.JwtToken;
import com.movewave.common.security.jwt.JwtTokenProvider;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Map;

/**
 * OAuth2 인증 성공 시 JWT 토큰을 생성하고 쿠키에 저장하는 핸들러
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class OAuth2SuccessHandler implements AuthenticationSuccessHandler {

    private final JwtTokenProvider jwtTokenProvider;
    private final RedirectProperties redirectProperties;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        if (!(authentication instanceof OAuth2AuthenticationToken)) {
            throw new IllegalArgumentException("지원하지 않는 인증 타입입니다.");
        }

        log.info("OAuth2 로그인 성공");

        OAuth2AuthenticationToken auth = (OAuth2AuthenticationToken) authentication;
        JwtToken jwtToken = jwtTokenProvider.createToken(auth);

        setTokenCookies(response, jwtToken);
        
        log.info("JWT 토큰 생성 및 쿠키 설정 완료");
        response.sendRedirect(redirectProperties.url());
    }

    /**
     * JWT 토큰을 쿠키에 설정
     */
    private void setTokenCookies(HttpServletResponse response, JwtToken jwtToken) {
        String accessTokenCookie = String.format(CookieConstants.COOKIE_ACCESS_TOKEN, jwtToken.getAccessToken());
        String refreshTokenCookie = String.format(CookieConstants.COOKIE_REFRESH_TOKEN, jwtToken.getRefreshToken());

        response.addHeader(HttpHeaders.SET_COOKIE, accessTokenCookie);
        response.addHeader(HttpHeaders.SET_COOKIE, refreshTokenCookie);
    }
}
