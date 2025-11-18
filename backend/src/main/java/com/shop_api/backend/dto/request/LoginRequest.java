package com.shop_api.backend.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Login request DTO with validation
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Request DTO cho đăng nhập")
public class LoginRequest {

    @NotBlank(message = "Email là bắt buộc")
    @Email(message = "Email phải hợp lệ")
    @Schema(description = "Email đăng nhập", example = "user@example.com", required = true)
    private String email;

    @NotBlank(message = "Mật khẩu là bắt buộc")
    @Schema(description = "Mật khẩu", example = "password123", required = true)
    private String password;
}

