package com.shop_api.backend.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
  @Override
  public void addCorsMappings(CorsRegistry registry) {
    registry.addMapping("/**")
        .allowedOriginPatterns( // Thay đổi từ allowedOrigins
            "http://localhost:3000",
            "http://localhost:8080",
            "https://*.ngrok-free.dev",
            "http://localhost:5173"
        // Đã xóa "*" khỏi đây
        )
        .allowedMethods("*")
        .allowedHeaders("*")
        .allowCredentials(true);
  }
}
