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

@Slf4j
@Component
@RequiredArgsConstructor
public class OAuth2SuccessHandler implements AuthenticationSuccessHandler {

    private final JwtTokenProvider jwtTokenProvider;

    private final RedirectProperties redirectProperties;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        log.info("로그인 성공!");

        OAuth2AuthenticationToken auth = (OAuth2AuthenticationToken) authentication;
        JwtToken jwtToken = jwtTokenProvider.createToken(auth);

        String accessToken = jwtToken.getAccessToken();
        String refreshToken = jwtToken.getRefreshToken();

        // 쿠키 생성 및 설정
        String accessTokenCookie = String.format(CookieConstants.COOKIE_ACCESS_TOKEN, accessToken);
        String refreshTokenCookie = String.format(CookieConstants.COOKIE_REFRESH_TOKEN, refreshToken);

        log.info(accessTokenCookie);
        log.info(refreshTokenCookie);

        response.addHeader(HttpHeaders.SET_COOKIE, accessTokenCookie);
        response.addHeader(HttpHeaders.SET_COOKIE, refreshTokenCookie);

        log.info("jwt 성공!");
        response.sendRedirect(redirectProperties.url());
    }
}
