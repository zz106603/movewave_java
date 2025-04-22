package com.movewave.common.util;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Optional;

public class HttpRequestInfoUtils {

    public static Optional<HttpServletRequest> getCurrentRequest() {
        return Optional.ofNullable(RequestContextHolder.getRequestAttributes())
                .filter(ServletRequestAttributes.class::isInstance)
                .map(ServletRequestAttributes.class::cast)
                .map(ServletRequestAttributes::getRequest);
    }

    public static String getMethod() {
        return getCurrentRequest()
                .map(HttpServletRequest::getMethod)
                .orElse("-");
    }

    public static String getUri() {
        return getCurrentRequest()
                .map(HttpServletRequest::getRequestURI)
                .orElse("-");
    }

    public static String getQueryString() {
        return getCurrentRequest()
                .map(HttpServletRequest::getQueryString)
                .orElse("");
    }

    public static String getRemoteAddr() {
        return getCurrentRequest()
                .map(HttpServletRequest::getRemoteAddr)
                .orElse("-");
    }

    public static String getUserAgent() {
        return getCurrentRequest()
                .map(req -> req.getHeader("User-Agent"))
                .orElse("-");
    }
}
