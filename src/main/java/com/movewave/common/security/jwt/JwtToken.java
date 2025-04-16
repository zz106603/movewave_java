package com.movewave.common.security.jwt;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

/**
 * JWT 토큰 정보를 담는 클래스입니다.
 * Access Token과 Refresh Token을 포함합니다.
 */
@ToString
@Getter
@Builder
public class JwtToken {
    
    /**
     * 토큰 타입 (Bearer)
     */
    private final String grantType;
    
    /**
     * 실제 인증에 사용되는 Access Token
     */
    private final String accessToken;
    
    /**
     * Access Token 재발급에 사용되는 Refresh Token
     */
    private final String refreshToken;

    /**
     * JSON 직렬화를 위한 생성자
     * @param grantType 토큰 타입 
     * @param accessToken Access Token 값
     * @param refreshToken Refresh Token 값
     */
    @JsonCreator
    public JwtToken(
            @JsonProperty("grantType") String grantType,
            @JsonProperty("accessToken") String accessToken,
            @JsonProperty("refreshToken") String refreshToken) {
        this.grantType = grantType;
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }
}
