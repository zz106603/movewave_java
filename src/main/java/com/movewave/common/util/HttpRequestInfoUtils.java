package com.movewave.common.util;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Optional;

public class HttpRequestInfoUtils {

    // 현재 HTTP 요청을 가져옵니다.
    // RequestContextHolder를 사용하여 현재 실행 중인 요청을 가져옵니다.
    // ServletRequestAttributes를 캐스팅하여 HttpServletRequest를 가져올 수 있습니다.
    public static Optional<HttpServletRequest> getCurrentRequest() {
        return Optional.ofNullable(RequestContextHolder.getRequestAttributes())
                .filter(ServletRequestAttributes.class::isInstance)
                .map(ServletRequestAttributes.class::cast)
                .map(ServletRequestAttributes::getRequest);
    }

    // HTTP 요청 메서드를 가져옵니다.
    public static String getMethod() {
        return getCurrentRequest()
                .map(HttpServletRequest::getMethod)
                .orElse("-");
    }

    // 요청 URI를 가져옵니다.
    public static String getUri() {
        return getCurrentRequest()
                .map(HttpServletRequest::getRequestURI)
                .orElse("-");
    }

    // 쿼리 문자열을 가져옵니다.
    public static String getQueryString() {
        return getCurrentRequest()
                .map(HttpServletRequest::getQueryString)
                .orElse("");
    }

    // 요청의 원격 주소(IP)를 가져옵니다.
    public static String getRemoteAddr() {
        return getCurrentRequest()
                .map(HttpServletRequest::getRemoteAddr)
                .orElse("-");
    }

    // 요청 헤더에서 User-Agent를 가져옵니다.
    public static String getUserAgent() {
        return getCurrentRequest()
                .map(req -> req.getHeader("User-Agent"))
                .orElse("-");
    }
}