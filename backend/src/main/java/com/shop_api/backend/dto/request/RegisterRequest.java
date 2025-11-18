package com.shop_api.backend.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
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
@Schema(description = "Request DTO cho đăng ký tài khoản mới")
public class RegisterRequest {

    @NotBlank(message = "Tên là bắt buộc")
    @Size(min = 2, max = 100, message = "Tên phải có từ 2 đến 100 ký tự")
    @Schema(description = "Tên khách hàng", example = "Nguyễn Văn A", required = true,
            minLength = 2, maxLength = 100)
    private String name;

    @NotBlank(message = "Email là bắt buộc")
    @Email(message = "Email phải hợp lệ")
    @Schema(description = "Email đăng ký", example = "user@example.com", required = true)
    private String email;

    @NotBlank(message = "Mật khẩu là bắt buộc")
    @Size(min = 6, message = "Mật khẩu phải có ít nhất 6 ký tự")
    @Schema(description = "Mật khẩu (tối thiểu 6 ký tự)", example = "password123", required = true,
            minLength = 6)
    private String password;

    @Schema(description = "Số điện thoại", example = "0123456789", required = false)
    private String phone;

    @Schema(description = "Địa chỉ", example = "123 Đường ABC, Quận XYZ", required = false)
    private String address;
}

