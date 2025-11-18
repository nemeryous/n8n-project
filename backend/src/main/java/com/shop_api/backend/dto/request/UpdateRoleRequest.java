package com.shop_api.backend.dto.request;

import com.shop_api.backend.constant.Role;
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
public class UpdateRoleRequest {

    @NotNull(message = "Role là bắt buộc")
    private Role role;
}

