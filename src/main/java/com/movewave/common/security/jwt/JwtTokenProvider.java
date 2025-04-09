package com.movewave.common.security.jwt;

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

@Slf4j
@Component
public class JwtTokenProvider {

    private final CustomUserDetailsService userDetailsService;

    private final Key key;

    private final JwtProperties jwtProperties;

    private static final String ACCOUNT_ID = "accountId";
    private static final String CLAIM_AUTHORITY = "auth";
    private static final long ONE_HOUR_IN_MILLISECONDS = 3_600_000L;
    private static final long ONE_DAY_IN_MILLISECONDS = 86_400_000L;

    public JwtTokenProvider(CustomUserDetailsService userDetailsService, JwtProperties jwtProperties){
        this.userDetailsService = userDetailsService;
        this.jwtProperties = jwtProperties;
        byte[] keyBytes = Decoders.BASE64.decode(jwtProperties.secret());
        this.key = Keys.hmacShaKeyFor(keyBytes);
    }

    public JwtToken createToken(OAuth2AuthenticationToken authenticationToken){
        String authorities = authenticationToken.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));

        CustomUserDetails customUserDetails = (CustomUserDetails) authenticationToken.getPrincipal();
        String loginId = customUserDetails.getUsername();
        Long accountId = customUserDetails.getAccountId();

        String accessToken = generateToken(loginId, authorities, accountId, ONE_HOUR_IN_MILLISECONDS);
        String refreshToken = generateToken(loginId, authorities, accountId, ONE_DAY_IN_MILLISECONDS);

        return JwtToken.builder()
                .grantType("Bearer")
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    public Authentication getAuthentication(String accessToken){
        try{
            Claims claims = parseClaims(accessToken);
            String loginId = claims.getSubject();

            String authClaim = claims.get(CLAIM_AUTHORITY, String.class);

            if(authClaim == null || authClaim.trim().isEmpty()){
                throw new InsufficientAuthenticationException("TOKEN_INVALID");
            }

            CustomUserDetails userDetails = (CustomUserDetails) userDetailsService.loadUserByUsername(loginId);

            Collection<? extends GrantedAuthority> authorities = Arrays.stream(authClaim.split(","))
                    .map(SimpleGrantedAuthority::new)
                    .collect(Collectors.toList());

            return new UsernamePasswordAuthenticationToken(userDetails, "", authorities);
        }catch(JwtException e) {
            throw new InsufficientAuthenticationException("TOKEN_INVALID");
        }
    }

    public boolean validateToken(String token){
        try{
            Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token);
            return true;
        }catch(JwtException e){
            log.error(e.getMessage());
            return false;
        }
    }

    public Claims parseClaims(String token){
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public String generateToken(String subject, String authorities, Long accountId, long expirationTime){
        return Jwts.builder()
                .setSubject(subject)
                .claim(CLAIM_AUTHORITY, authorities)
                .claim(ACCOUNT_ID, accountId)
                .setExpiration(new Date(System.currentTimeMillis() + expirationTime))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    public JwtToken refreshAccessToken(String refreshToken){
        try{
            if(!validateToken(refreshToken)){
                throw new InsufficientAuthenticationException("TOKEN_INVALID");
            }

            Claims claims = parseClaims(refreshToken);
            Authentication authentication = getAuthentication(refreshToken);

            if(authentication == null || !authentication.isAuthenticated()){
                throw new InsufficientAuthenticationException("TOKEN_INVALID");
            }

            CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
            String loginId = userDetails.getUsername();
            Long accountId = userDetails.getAccountId();
            String authorities = claims.get(CLAIM_AUTHORITY, String.class);

            String newAccessToken = generateToken(loginId, authorities, accountId, ONE_HOUR_IN_MILLISECONDS);
            String newRefreshToken = generateToken(loginId, authorities, accountId, ONE_DAY_IN_MILLISECONDS);

            return JwtToken.builder()
                    .grantType("Bearer")
                    .accessToken(newAccessToken)
                    .refreshToken(newRefreshToken)
                    .build();
        }catch(JwtException e){
            throw new InsufficientAuthenticationException("TOKEN_INVALID");
        }
    }
}
