package com.shop_api.backend.config;

import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * SpringDoc Configuration to disable Spring Data REST provider Follows Single Responsibility
 * Principle
 */
@Configuration
public class SpringDocConfig {

    /**
     * Configure SpringDoc to only scan our API controllers This disables Spring Data REST
     * auto-detection by explicitly matching only our API paths
     */
    @Bean
    public GroupedOpenApi publicApi() {
        return GroupedOpenApi.builder().group("shop-api").pathsToMatch("/api/v1/**").build();
    }

}

