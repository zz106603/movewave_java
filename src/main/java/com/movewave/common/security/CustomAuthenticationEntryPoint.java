package com.movewave.common.security;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * 인증되지 않은 사용자의 접근을 처리하는 커스텀 EntryPoint입니다.
 * Spring Security에서 인증되지 않은 요청을 처리할 때 사용됩니다.
 */
@Slf4j
@Component
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException {
        // JSON 응답 설정
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());
        
        try {
            // 401 Unauthorized 상태 코드 설정
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write(authException.getMessage());
        } catch (IOException e) {
            // IOException 발생 시 로그 기록 및 500 에러 응답
            log.error("Error writing response: {}", e.getMessage());
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("SERVER_ERROR");
        }
    }
}
