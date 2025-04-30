package com.movewave.common.exception.base;

import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.security.authentication.InsufficientAuthenticationException;

/**
 * 에러 메시지를 정의하는 열거형 클래스입니다.
 * 각 에러는 고유한 코드와 메시지를 가집니다.
 */
@AllArgsConstructor
@Getter
public enum ErrorMessages {

    /**
     * 잘못된 요청에 대한 에러
     * @see IllegalArgumentException
     */
    BAD_REQUEST("E400-001", "잘못된 요청입니다."),

    /**
     * 엔티티를 찾을 수 없을 때의 에러
     * @see EntityNotFoundException
     */
    NOT_FOUND_ENTITY("E404-001", "%s(ID: %s)를 찾을 수 없습니다."),

    /**
     * 토큰이 유효하지 않을 때의 에러
     * @see InsufficientAuthenticationException
     */
    TOKEN_INVALID("E401-002", "유효하지 않은 토큰입니다.");

    /**
     * 에러 코드
     * 형식: E{HTTP 상태코드}-{일련번호}
     */
    private final String code;

    /**
     * 에러 메시지
     * 포맷 문자열을 포함할 수 있음
     */
    private final String message;

    /**
     * 메시지에 인자를 적용하여 포맷된 문자열을 반환합니다.
     * @param args 메시지 포맷에 적용할 인자들
     * @return 포맷된 에러 메시지
     */
    public String format(Object... args) {
        return String.format(this.message, args);
    }

}