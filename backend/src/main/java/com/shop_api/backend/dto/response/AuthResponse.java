package com.shop_api.backend.dto.response;

import com.shop_api.backend.constant.Role;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Authentication response containing JWT tokens
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Response chứa JWT tokens và thông tin user sau khi đăng nhập/đăng ký")
public class AuthResponse {

    @Schema(description = "Access token JWT", example = "eyJhbGciOiJIUzUxMiJ9...")
    private String accessToken;

    @Schema(description = "Refresh token JWT", example = "eyJhbGciOiJIUzUxMiJ9...")
    private String refreshToken;

    @Builder.Default
    @Schema(description = "Loại token", example = "Bearer")
    private String tokenType = "Bearer";

    @Schema(description = "Thông tin user")
    private UserInfo user;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(description = "Thông tin user")
    public static class UserInfo {
        @Schema(description = "ID khách hàng", example = "1")
        private Integer id;

        @Schema(description = "Tên khách hàng", example = "Nguyễn Văn A")
        private String name;

        @Schema(description = "Email", example = "user@example.com")
        private String email;

        @Schema(description = "Số điện thoại", example = "0123456789")
        private String phone;

        @Schema(description = "Địa chỉ", example = "123 Đường ABC")
        private String address;

        @Schema(description = "Role của user", example = "USER",
                allowableValues = {"USER", "ADMIN"})
        private Role role;
    }
}

