package com.movewave.common.security.jwt;

import com.movewave.common.exception.base.ErrorMessages;
import com.movewave.common.properties.JwtProperties;
import com.movewave.common.security.CustomUserDetails;
import com.movewave.common.security.CustomUserDetailsService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.stereotype.Component;
import java.security.Key;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;

/**
 * JWT 토큰 생성, 검증 및 관리를 담당하는 클래스
 */
@Slf4j
@Component
public class JwtTokenProvider {

    private static final String ACCOUNT_ID = "accountId";
    private static final String CLAIM_AUTHORITY = "auth";
    private static final String GRANT_TYPE = "Bearer";
    private static final long ONE_HOUR_IN_MILLISECONDS = 3_600_000L;
    private static final long ONE_DAY_IN_MILLISECONDS = 86_400_000L;

    private final CustomUserDetailsService userDetailsService; // 사용자 정보 조회 서비스
    private final Key key; // JWT 서명에 사용되는 키

    /**
     * JwtTokenProvider 생성자
     * @param userDetailsService 사용자 상세 정보 서비스
     * @param jwtProperties JWT 설정 정보
     */
    public JwtTokenProvider(CustomUserDetailsService userDetailsService, JwtProperties jwtProperties) {
        this.userDetailsService = userDetailsService;
        // JWT 서명 키 초기화 메서드 호출
        this.key = initializeKey(jwtProperties.secret());
    }

    /**
     * JWT 서명 키 초기화
     * @param secret JWT 비밀키
     * @return 초기화된 Key 객체
     */
    private Key initializeKey(String secret) {
        // Base64로 인코딩된 비밀키를 디코딩
        byte[] keyBytes = Decoders.BASE64.decode(secret);
        // HMAC-SHA 알고리즘용 키 생성
        return Keys.hmacShaKeyFor(keyBytes);
    }

    /**
     * OAuth2 인증 토큰으로부터 JWT 토큰 생성
     * @param authenticationToken OAuth2 인증 토큰
     * @return 생성된 JWT 토큰 쌍
     */
    public JwtToken createToken(OAuth2AuthenticationToken authenticationToken) {
        CustomUserDetails userDetails = (CustomUserDetails) authenticationToken.getPrincipal();
        // OAuth2 토큰에서 권한 정보 추출
        String authorities = extractAuthorities(authenticationToken);
        
        // 액세스/리프레시 토큰 쌍 생성
        return createTokenPair(userDetails.getUsername(), authorities, userDetails.getAccountId());
    }

    /**
     * 인증 토큰에서 권한 정보 추출
     * @param token OAuth2 인증 토큰
     * @return 권한 문자열
     */
    private String extractAuthorities(OAuth2AuthenticationToken token) {
        // 권한 정보를 쉼표로 구분된 문자열로 변환
        return token.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));
    }

    /**
     * 액세스 토큰과 리프레시 토큰 쌍 생성
     * @param loginId 로그인 ID
     * @param authorities 권한 정보
     * @param accountId 계정 ID
     * @return JWT 토큰 쌍
     */
    private JwtToken createTokenPair(String loginId, String authorities, Long accountId) {
        // 1시간 유효한 액세스 토큰 생성
        String accessToken = generateToken(loginId, authorities, accountId, ONE_HOUR_IN_MILLISECONDS);
        // 1일 유효한 리프레시 토큰 생성
        String refreshToken = generateToken(loginId, authorities, accountId, ONE_DAY_IN_MILLISECONDS);

        // JWT 토큰 객체 생성 및 반환
        return JwtToken.builder()
                .grantType(GRANT_TYPE)
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    /**
     * 액세스 토큰으로부터 인증 정보 추출
     * @param accessToken 액세스 토큰
     * @return 인증 객체
     * @throws InsufficientAuthenticationException 토큰이 유효하지 않은 경우
     */
    public Authentication getAuthentication(String accessToken) {
        try {
            // 토큰에서 클레임 정보 파싱
            Claims claims = parseClaims(accessToken);
            // 권한 정보 유효성 검사
            validateAuthorityClaim(claims);

            String loginId = claims.getSubject();
            // 사용자 정보 조회
            CustomUserDetails userDetails = (CustomUserDetails) userDetailsService.loadUserByUsername(loginId);
            // 클레임에서 권한 정보 추출
            Collection<? extends GrantedAuthority> authorities = getAuthoritiesFromClaim(claims);

            // Spring Security 인증 객체 생성
            return new UsernamePasswordAuthenticationToken(userDetails, "", authorities);
        } catch (JwtException e) {
            throw new InsufficientAuthenticationException(ErrorMessages.TOKEN_INVALID.getMessage());
        }
    }

    /**
     * Claims에서 권한 정보 검증
     * @param claims JWT Claims
     * @throws InsufficientAuthenticationException 권한 정보가 없거나 유효하지 않은 경우
     */
    private void validateAuthorityClaim(Claims claims) {
        String authClaim = claims.get(CLAIM_AUTHORITY, String.class);
        if (authClaim == null || authClaim.trim().isEmpty()) {
            throw new InsufficientAuthenticationException(ErrorMessages.TOKEN_INVALID.getMessage());
        }
    }

    /**
     * Claims에서 권한 정보 추출
     * @param claims JWT Claims
     * @return 권한 컬렉션
     */
    private Collection<? extends GrantedAuthority> getAuthoritiesFromClaim(Claims claims) {
        // 클레임에서 권한 정보 문자열 추출
        String authClaim = claims.get(CLAIM_AUTHORITY, String.class);
        // 쉼표로 구분된 권한 문자열을 GrantedAuthority 컬렉션으로 변환
        return Arrays.stream(authClaim.split(","))
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }

    /**
     * 토큰 유효성 검증
     * @param token 검증할 토큰
     * @return 유효성 여부
     */
    public boolean validateToken(String token) {
        try {
            // JWT 파서를 사용하여 토큰 검증
            Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (JwtException e) {
            log.error("Token validation failed: {}", e.getMessage());
            return false;
        }
    }

    /**
     * 토큰에서 Claims 추출
     * @param token JWT 토큰
     * @return Claims 객체
     */
    public Claims parseClaims(String token) {
        // JWT 파서를 사용하여 토큰의 클레임 정보 추출
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    /**
     * JWT 토큰 생성
     * @param subject 토큰 제목
     * @param authorities 권한 정보
     * @param accountId 계정 ID
     * @param expirationTime 만료 시간
     * @return 생성된 JWT 토큰
     */
    public String generateToken(String subject, String authorities, Long accountId, long expirationTime) {
        Date expiration = new Date(System.currentTimeMillis() + expirationTime);
        // JWT 빌더를 사용하여 토큰 생성
        return Jwts.builder()
                .setSubject(subject)
                .claim(CLAIM_AUTHORITY, authorities)
                .claim(ACCOUNT_ID, accountId)
                .setExpiration(expiration)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    /**
     * 리프레시 토큰을 사용하여 새로운 액세스 토큰 발급
     * @param refreshToken 리프레시 토큰
     * @return 새로운 JWT 토큰 쌍
     * @throws InsufficientAuthenticationException 토큰이 유효하지 않은 경우
     */
    public JwtToken refreshAccessToken(String refreshToken) {
        try {
            // 리프레시 토큰 유효성 검사
            validateRefreshTokenAndAuthentication(refreshToken);
            
            // 토큰에서 클레임 정보 추출
            Claims claims = parseClaims(refreshToken);
            // 토큰에서 인증 정보 추출
            Authentication authentication = getAuthentication(refreshToken);
            CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
            
            // 새로운 토큰 쌍 생성
            return createTokenPair(
                    userDetails.getUsername(),
                    claims.get(CLAIM_AUTHORITY, String.class),
                    userDetails.getAccountId()
            );
        } catch (JwtException e) {
            throw new InsufficientAuthenticationException(ErrorMessages.TOKEN_INVALID.getMessage());
        }
    }

    /**
     * 리프레시 토큰과 인증 정보 검증
     * @param refreshToken 리프레시 토큰
     * @throws InsufficientAuthenticationException 토큰이나 인증이 유효하지 않은 경우
     */
    private void validateRefreshTokenAndAuthentication(String refreshToken) {
        // 토큰 유효성 검사
        if (!validateToken(refreshToken)) {
            throw new InsufficientAuthenticationException(ErrorMessages.TOKEN_INVALID.getMessage());
        }

        // 인증 정보 유효성 검사
        Authentication authentication = getAuthentication(refreshToken);
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new InsufficientAuthenticationException(ErrorMessages.TOKEN_INVALID.getMessage());
        }
    }
}
