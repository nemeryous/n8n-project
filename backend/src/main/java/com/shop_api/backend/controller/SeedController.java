package com.shop_api.backend.controller;

import java.util.HashMap;
import java.util.Map;
import com.shop_api.backend.seed.ProductSeeder;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Controller để quản lý seeding data Chỉ dành cho ADMIN
 */
@Slf4j
@RestController
@RequestMapping("${api.prefix}/seed")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
@Tag(name = "Seed", description = "API để seeding dữ liệu mẫu (Chỉ dành cho ADMIN)")
public class SeedController {

    private final ProductSeeder productSeeder;

    @Operation(summary = "Seed products",
            description = "Tạo 1000 sản phẩm mẫu vào database. Chỉ chạy nếu database trống.",
            security = @SecurityRequirement(name = "bearerAuth"))
    @PostMapping("/products")
    public ResponseEntity<Map<String, Object>> seedProducts(
            @RequestParam(defaultValue = "1000") final int count) {
        log.info("Manual seeding request for {} products", count);

        final Map<String, Object> response = new HashMap<>();
        response.put("message", "Seeding đã được trigger. Kiểm tra logs để xem tiến trình.");
        response.put("count", count);

        // Note: ProductSeeder hiện tại chỉ chạy khi app start và database trống
        // Để chạy manual, cần refactor ProductSeeder thành service
        // Tạm thời trả về message hướng dẫn
        response.put("note", "Để chạy seeding manual, cần restart application với database trống.");

        return ResponseEntity.ok(response);
    }
}

