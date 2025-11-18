package com.shop_api.backend.dto.request;

import com.shop_api.backend.constant.Role;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Request DTO for updating user role
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Request DTO để cập nhật role của khách hàng")
public class UpdateRoleRequest {

    @NotNull(message = "Role là bắt buộc")
    @Schema(description = "Role mới của khách hàng", example = "ADMIN", required = true,
            allowableValues = {"USER", "ADMIN"})
    private Role role;
}

