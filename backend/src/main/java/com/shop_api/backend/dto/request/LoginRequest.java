package com.shop_api.backend.dto.request;

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
public class LoginRequest {

    @NotBlank(message = "Email là bắt buộc")
    @Email(message = "Email phải hợp lệ")
    private String email;

    @NotBlank(message = "Mật khẩu là bắt buộc")
    private String password;
}

