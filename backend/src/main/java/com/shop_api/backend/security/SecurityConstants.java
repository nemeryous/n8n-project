package com.shop_api.backend.security;

/**
 * Security constants following best practices Centralized configuration following DRY principle
 */
public final class SecurityConstants {

    private SecurityConstants() {
        // Prevent instantiation
    }

    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String HEADER_STRING = "Authorization";
    public static final String[] PUBLIC_URLS =
            {"/api/v1/auth/**", "/api/v1/public/**", "/api-docs/**", "/swagger-ui/**",
                    "/swagger-ui.html", "/v3/api-docs/**", "/actuator/**"};
}

