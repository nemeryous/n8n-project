package com.shop_api.backend.security;

import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.stream.Collectors;
import javax.crypto.SecretKey;
import com.shop_api.backend.exception.UnauthorizedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * JWT Token Provider following Single Responsibility Principle Handles JWT token generation,
 * validation, and parsing
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class JwtTokenProvider {

    private final JwtProperties jwtProperties;
    private SecretKey secretKey;

    @PostConstruct
    protected void init() {
        // Initialize secret key after properties are loaded
        this.secretKey =
                Keys.hmacShaKeyFor(jwtProperties.getSecret().getBytes(StandardCharsets.UTF_8));
    }

    /**
     * Generate JWT token from authentication object
     */
    public String generateToken(Authentication authentication) {
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + jwtProperties.getExpirationMs());

        String authorities = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority).collect(Collectors.joining(","));

        return Jwts.builder().subject(String.valueOf(userPrincipal.getId()))
                .claim("email", userPrincipal.getEmail()).claim("name", userPrincipal.getName())
                .claim("authorities", authorities).issuedAt(now).expiration(expiryDate)
                .signWith(secretKey, Jwts.SIG.HS512).compact();
    }

    /**
     * Generate JWT token from user ID
     */
    public String generateTokenFromUserId(Integer userId, String email, String name, String role) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + jwtProperties.getExpirationMs());

        return Jwts.builder().subject(String.valueOf(userId)).claim("email", email)
                .claim("name", name).claim("role", role).issuedAt(now).expiration(expiryDate)
                .signWith(secretKey, Jwts.SIG.HS512).compact();
    }

    /**
     * Generate refresh token
     */
    public String generateRefreshToken(Integer userId) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + jwtProperties.getRefreshExpirationMs());

        return Jwts.builder().subject(String.valueOf(userId)).issuedAt(now).expiration(expiryDate)
                .signWith(secretKey, Jwts.SIG.HS512).compact();
    }

    /**
     * Get user ID from JWT token
     */
    public Integer getUserIdFromToken(String token) {
        Claims claims = parseToken(token);
        return Integer.parseInt(claims.getSubject());
    }

    /**
     * Get email from JWT token
     */
    public String getEmailFromToken(String token) {
        Claims claims = parseToken(token);
        return claims.get("email", String.class);
    }

    /**
     * Validate JWT token (check signature, format, and expiration) Used for access tokens
     */
    public boolean validateToken(String token) {
        try {
            parseToken(token);
            return true;
        } catch (SignatureException ex) {
            log.error("Invalid JWT signature: {}", ex.getMessage());
            throw new UnauthorizedException("Chữ ký JWT không hợp lệ");
        } catch (MalformedJwtException ex) {
            log.error("Invalid JWT token: {}", ex.getMessage());
            throw new UnauthorizedException("Token JWT không hợp lệ");
        } catch (ExpiredJwtException ex) {
            log.error("Expired JWT token: {}", ex.getMessage());
            throw new UnauthorizedException("Token JWT đã hết hạn");
        } catch (UnsupportedJwtException ex) {
            log.error("Unsupported JWT token: {}", ex.getMessage());
            throw new UnauthorizedException("Token JWT không được hỗ trợ");
        } catch (IllegalArgumentException ex) {
            log.error("JWT claims string is empty: {}", ex.getMessage());
            throw new UnauthorizedException("Chuỗi claims JWT trống");
        }
    }

    /**
     * Validate refresh token structure only (signature and format) Does NOT check expiration -
     * expiration is checked from database Used for refresh tokens
     */
    public boolean validateRefreshTokenStructure(String token) {
        try {
            // Parse token without checking expiration
            // We only validate signature and format here
            Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token);
            return true;
        } catch (ExpiredJwtException ex) {
            // For refresh token, we allow expired JWT here
            // Expiration will be checked from database
            log.debug("Refresh token JWT expired, but will check database expiration");
            return true;
        } catch (SignatureException ex) {
            log.error("Invalid refresh token signature: {}", ex.getMessage());
            throw new UnauthorizedException("Chữ ký refresh token không hợp lệ");
        } catch (MalformedJwtException ex) {
            log.error("Invalid refresh token format: {}", ex.getMessage());
            throw new UnauthorizedException("Định dạng refresh token không hợp lệ");
        } catch (UnsupportedJwtException ex) {
            log.error("Unsupported refresh token: {}", ex.getMessage());
            throw new UnauthorizedException("Refresh token không được hỗ trợ");
        } catch (IllegalArgumentException ex) {
            log.error("Refresh token claims string is empty: {}", ex.getMessage());
            throw new UnauthorizedException("Chuỗi claims refresh token trống");
        }
    }

    /**
     * Parse JWT token and get claims
     */
    private Claims parseToken(String token) {
        return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload();
    }
}

