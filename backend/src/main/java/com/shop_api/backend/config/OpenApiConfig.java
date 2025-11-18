package com.shop_api.backend.config;

import java.util.List;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;

/**
 * OpenAPI (Swagger) Configuration Follows Single Responsibility Principle - only handles API
 * documentation configuration
 */
@Configuration
public class OpenApiConfig {

    @Value("${server.port:8080}")
    private String serverPort;

    @Bean
    public OpenAPI customOpenAPI() {
        final String securitySchemeName = "bearerAuth";

        return new OpenAPI().info(buildApiInfo()).servers(buildServers())
                .components(new Components().addSecuritySchemes(securitySchemeName,
                        buildSecurityScheme()))
                .addSecurityItem(new SecurityRequirement().addList(securitySchemeName));
    }

    /**
     * Build API information Follows DRY principle
     */
    private Info buildApiInfo() {
        return new Info().title("Shop API Documentation").version("1.0.0").description("""
                API Documentation cho hệ thống Shop Management System.

                ## Authentication
                Hệ thống sử dụng JWT Bearer Token authentication.

                ### Cách sử dụng:
                1. Đăng ký/Đăng nhập để nhận access token và refresh token
                2. Sử dụng access token trong header: `Authorization: Bearer <access-token>`
                3. Khi access token hết hạn, sử dụng refresh token để lấy token mới

                ## Roles
                - **USER**: Người dùng thông thường
                - **ADMIN**: Quản trị viên (có quyền truy cập các endpoint admin)
                """)
                .contact(new Contact().name("Shop API Support").email("support@shopapi.com")
                        .url("https://shopapi.com"))
                .license(new License().name("Apache 2.0")
                        .url("https://www.apache.org/licenses/LICENSE-2.0.html"));
    }

    /**
     * Build server list Follows DRY principle
     */
    private List<Server> buildServers() {
        return List.of(
                new Server().url("http://localhost:" + serverPort)
                        .description("Local Development Server"),
                new Server().url("https://api.shopapi.com").description("Production Server"));
    }

    /**
     * Build security scheme for JWT Follows DRY principle
     */
    private SecurityScheme buildSecurityScheme() {
        return new SecurityScheme().name("bearerAuth").type(SecurityScheme.Type.HTTP)
                .scheme("bearer").bearerFormat("JWT")
                .description("JWT Authentication. Nhập token không cần prefix 'Bearer'");
    }
}

