package com.movewave.common.security.jwt;

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

@Slf4j
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends GenericFilterBean {

    private final JwtTokenProvider jwtTokenProvider;

    private boolean isPermitAllEndpoint(String requestURI){
        return requestURI.startsWith("/swagger-ui") || requestURI.startsWith("/v3/api-docs");
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        String requestURI = httpRequest.getRequestURI();

        if(isPermitAllEndpoint(requestURI)){
            handlePermitAllEndpoint(chain, request, response);
            return;
        }

        try{
            String accessToken = resolveAccessAndRefreshToken(httpRequest, "accessToken");
            if(accessToken != null && jwtTokenProvider.validateToken(accessToken)){
                setAuthentication(accessToken);
            }else{
                handleRefreshToken(httpRequest, httpResponse);
            }
        }catch(AuthenticationException e){
            SecurityContextHolder.clearContext();
        }finally{
            chain.doFilter(request, response);
        }
    }

    private void handlePermitAllEndpoint(FilterChain chain, ServletRequest request, ServletResponse response) throws IOException, ServletException{
        chain.doFilter(request, response);
    }

    private void setAuthentication(String accessToken){
        Authentication authentication = jwtTokenProvider.getAuthentication(accessToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    private void handleRefreshToken(HttpServletRequest httpRequest, HttpServletResponse httpResponse){
        try{
            String refreshToken = resolveAccessAndRefreshToken(httpRequest, "refreshToken");

            if(refreshToken == null){
                throw new InsufficientAuthenticationException("TOKEN_INVALID");
            }

            if(jwtTokenProvider.validateToken(refreshToken)){
                JwtToken newToken = jwtTokenProvider.refreshAccessToken(refreshToken);
                setCookies(httpResponse, newToken);
                setAuthentication(newToken.getAccessToken());
            }else{
                throw new InsufficientAuthenticationException("TOKEN_INVALID");
            }
        }catch(AuthenticationException e){
            SecurityContextHolder.clearContext();
            throw e;
        }
    }

    private void setCookies(HttpServletResponse httpResponse, JwtToken newToken){
        String accessTokenCookie = String.format(
                CookieConstants.COOKIE_ACCESS_TOKEN,
                newToken.getAccessToken()
        );

        String refreshTokenCookie = String.format(
                CookieConstants.COOKIE_REFRESH_TOKEN,
                newToken.getRefreshToken()
        );

        httpResponse.addHeader(HttpHeaders.SET_COOKIE, accessTokenCookie);
        httpResponse.addHeader(HttpHeaders.SET_COOKIE, refreshTokenCookie);
    }

    private String resolveAccessAndRefreshToken(HttpServletRequest httpRequest, String tokenType){
        Cookie[] cookies = httpRequest.getCookies();
        if(cookies != null){
            for(Cookie cookie : cookies){
                if(tokenType.equals(cookie.getName())){
                    return cookie.getValue();
                }
            }
        }
        return null;
    }
}
