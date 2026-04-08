package com.tugas.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
@Slf4j
public class JwtTokenProvider {

    @Value("${app.jwt.secret:mySecretKeyForJWTTokenGenerationAndValidationPurposeOnly123456789}")
    private String jwtSecret;

    @Value("${app.jwt.expiration:86400000}")
    private long jwtExpirationMs;

    /**
     * Generate JWT token dari Authentication object
     */
    public String generateToken(Authentication authentication) {
        String username = authentication.getName();
        
        Map<String, Object> claims = new HashMap<>();
        claims.put("authorities", authentication.getAuthorities());
        
        return createToken(claims, username, (long) jwtExpirationMs);
    }

    /**
     * Generate JWT token dari username
     */
    public String generateTokenFromUsername(String username) {
        Map<String, Object> claims = new HashMap<>();
        return createToken(claims, username, (long) jwtExpirationMs);
    }

    /**
     * Generate JWT token dari username (alias for test compatibility)
     */
    public String generateToken(String username) {
        Map<String, Object> claims = new HashMap<>();
        return createToken(claims, username, (long) jwtExpirationMs);
    }

    /**
     * Generate JWT token dengan custom expiration time (untuk testing)
     */
    public String generateTokenWithExpiration(String username, long expirationMs) {
        Map<String, Object> claims = new HashMap<>();
        return createToken(claims, username, expirationMs);
    }

    /**
     * Create JWT token
     */
    private String createToken(Map<String, Object> claims, String subject, Long expirationTime) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + expirationTime);

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(SignatureAlgorithm.HS512, jwtSecret.getBytes())
                .compact();
    }

    /**
     * Get username dari JWT token
     */
    public String getUsernameFromJwt(String token) {
        try {
            return Jwts.parser()
                    .setSigningKey(jwtSecret.getBytes())
                    .parseClaimsJws(token)
                    .getBody()
                    .getSubject();
        } catch (ExpiredJwtException e) {
            log.error("JWT token sudah expired", e);
        } catch (UnsupportedJwtException e) {
            log.error("JWT token tidak didukung", e);
        } catch (MalformedJwtException e) {
            log.error("JWT token tidak valid", e);
        } catch (SignatureException e) {
            log.error("JWT signature tidak valid", e);
        } catch (IllegalArgumentException e) {
            log.error("JWT claims string kosong", e);
        }
        return null;
    }

    /**
     * Validasi JWT token
     */
    public boolean validateToken(String token) {
        try {
            Jwts.parser()
                    .setSigningKey(jwtSecret.getBytes())
                    .parseClaimsJws(token);
            return true;
        } catch (ExpiredJwtException e) {
            log.error("JWT token sudah expired", e);
        } catch (UnsupportedJwtException e) {
            log.error("JWT token tidak didukung", e);
        } catch (MalformedJwtException e) {
            log.error("JWT token tidak valid", e);
        } catch (SignatureException e) {
            log.error("JWT signature tidak valid", e);
        } catch (IllegalArgumentException e) {
            log.error("JWT claims string kosong", e);
        }
        return false;
    }

    /**
     * Get token dari Authorization header
     */
    public String getTokenFromBearerString(String bearerToken) {
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }
}
