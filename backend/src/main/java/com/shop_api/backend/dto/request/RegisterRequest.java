package com.shop_api.backend.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Registration request DTO with validation
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegisterRequest {

    @NotBlank(message = "Tên là bắt buộc")
    @Size(min = 2, max = 100, message = "Tên phải có từ 2 đến 100 ký tự")
    private String name;

    @NotBlank(message = "Email là bắt buộc")
    @Email(message = "Email phải hợp lệ")
    private String email;

    @NotBlank(message = "Mật khẩu là bắt buộc")
    @Size(min = 6, message = "Mật khẩu phải có ít nhất 6 ký tự")
    private String password;

    private String phone;
    private String address;
}

