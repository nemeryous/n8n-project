package com.shop_api.backend.security;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import lombok.Getter;
import lombok.Setter;

/**
 * JWT configuration properties Follows externalized configuration principle
 */
@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "jwt")
public class JwtProperties {
    private String secret;
    private long expirationMs;
    private long refreshExpirationMs;
}

