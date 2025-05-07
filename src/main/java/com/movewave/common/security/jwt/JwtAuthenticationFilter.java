package com.movewave.common.security.jwt;

import com.movewave.common.exception.base.ErrorMessages;
import com.movewave.common.security.constants.CookieConstants;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.GenericFilterBean;

import java.io.IOException;

/**
 * JWT 토큰 기반 인증을 처리하는 필터
 * 요청에서 JWT 토큰을 추출하고 검증하여 인증을 처리합니다.
 */
@Slf4j
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends GenericFilterBean {

    private static final String ACCESS_TOKEN_NAME = "accessToken";
    private static final String REFRESH_TOKEN_NAME = "refreshToken";

    private final JwtTokenProvider jwtTokenProvider;

    /**
     * 필터 체인에서 JWT 토큰 인증을 처리합니다.
     * @param request 서블릿 요청
     * @param response 서블릿 응답
     * @param chain 필터 체인
     */
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) 
            throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        try {
            processAuthentication(httpRequest, httpResponse);
        } catch (AuthenticationException e) {
            log.error("Authentication failed: {}", e.getMessage());
            SecurityContextHolder.clearContext();
        } finally {
            chain.doFilter(request, response);
        }
    }

    /**
     * 인증 처리 로직을 수행합니다.
     * @param request HTTP 요청
     * @param response HTTP 응답
     */
    private void processAuthentication(HttpServletRequest request, HttpServletResponse response) {
        if (isPermitAllEndpoint(request.getRequestURI())) {
            return;
        }

        String accessToken = extractToken(request, ACCESS_TOKEN_NAME);
        if (isValidAccessToken(accessToken)) {
            authenticateWithAccessToken(accessToken);
        } else {
            processRefreshToken(request, response);
        }
    }

    /**
     * 인증이 필요없는 엔드포인트인지 확인합니다.
     * @param requestURI 요청 URI
     * @return 인증 불필요 여부
     */
    private boolean isPermitAllEndpoint(String requestURI) {
        return requestURI.startsWith("/swagger-ui") || 
               requestURI.startsWith("/v3/api-docs");
    }

    /**
     * 쿠키에서 토큰을 추출합니다.
     * @param request HTTP 요청
     * @param tokenType 토큰 타입 (accessToken 또는 refreshToken)
     * @return 추출된 토큰 값
     */
    private String extractToken(HttpServletRequest request, String tokenType) {
        Cookie[] cookies = request.getCookies();
        if (cookies == null) {
            return null;
        }

        return java.util.Arrays.stream(cookies)
                .filter(cookie -> tokenType.equals(cookie.getName()))
                .map(Cookie::getValue)
                .findFirst()
                .orElse(null);
    }

    /**
     * 액세스 토큰의 유효성을 검사합니다.
     * @param token 검사할 토큰
     * @return 토큰 유효성 여부
     */
    private boolean isValidAccessToken(String token) {
        return token != null && jwtTokenProvider.validateToken(token);
    }

    /**
     * 액세스 토큰으로 인증을 수행합니다.
     * @param accessToken 액세스 토큰
     */
    private void authenticateWithAccessToken(String accessToken) {
        Authentication authentication = jwtTokenProvider.getAuthentication(accessToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    /**
     * 리프레시 토큰 처리를 수행합니다.
     * @param request HTTP 요청
     * @param response HTTP 응답
     */
    private void processRefreshToken(HttpServletRequest request, HttpServletResponse response) {
        String refreshToken = extractToken(request, REFRESH_TOKEN_NAME);
        validateRefreshToken(refreshToken);

        if (jwtTokenProvider.validateToken(refreshToken)) {
            refreshAuthentication(refreshToken, response);
        } else {
            throw new InsufficientAuthenticationException(ErrorMessages.TOKEN_INVALID.getMessage());
        }
    }

    /**
     * 리프레시 토큰의 존재 여부를 검증합니다.
     * @param refreshToken 리프레시 토큰
     */
    private void validateRefreshToken(String refreshToken) {
        if (refreshToken == null) {
            throw new InsufficientAuthenticationException(ErrorMessages.TOKEN_INVALID.getMessage());
        }
    }

    /**
     * 리프레시 토큰을 사용하여 새로운 인증을 수행합니다.
     * @param refreshToken 리프레시 토큰
     * @param response HTTP 응답
     */
    private void refreshAuthentication(String refreshToken, HttpServletResponse response) {
        JwtToken newToken = jwtTokenProvider.refreshAccessToken(refreshToken);
        updateTokenCookies(response, newToken);
        authenticateWithAccessToken(newToken.getAccessToken());
    }

    /**
     * 새로운 토큰으로 쿠키를 업데이트합니다.
     * @param response HTTP 응답
     * @param token 새로운 JWT 토큰
     */
    private void updateTokenCookies(HttpServletResponse response, JwtToken token) {
        String accessTokenCookie = String.format(
                CookieConstants.COOKIE_ACCESS_TOKEN,
                token.getAccessToken()
        );
        String refreshTokenCookie = String.format(
                CookieConstants.COOKIE_REFRESH_TOKEN,
                token.getRefreshToken()
        );

        response.addHeader(HttpHeaders.SET_COOKIE, accessTokenCookie);
        response.addHeader(HttpHeaders.SET_COOKIE, refreshTokenCookie);
    }
}
