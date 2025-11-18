package com.shop_api.backend.controller;

import com.shop_api.backend.dto.request.LoginRequest;
import com.shop_api.backend.dto.request.RegisterRequest;
import com.shop_api.backend.dto.response.ApiResponse;
import com.shop_api.backend.dto.response.AuthResponse;
import com.shop_api.backend.security.JwtTokenProvider;
import com.shop_api.backend.service.auth.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Authentication controller Handles authentication endpoints
 */
@Slf4j
@RestController
@RequestMapping("${api.prefix}/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final JwtTokenProvider tokenProvider;

    /**
     * Register a new customer
     */
    @PostMapping("/register")
    public ResponseEntity<ApiResponse<AuthResponse>> register(
            @Valid @RequestBody RegisterRequest request) {
        log.info("POST /auth/register - Registering new customer");

        AuthResponse response = authService.register(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Đăng ký thành công", response));
    }

    /**
     * Login customer
     */
    @PostMapping("/login")
    public ResponseEntity<ApiResponse<AuthResponse>> login(
            @Valid @RequestBody LoginRequest request) {
        log.info("POST /auth/login - Authenticating customer");

        AuthResponse response = authService.login(request);
        return ResponseEntity.ok(ApiResponse.success("Đăng nhập thành công", response));
    }

    /**
     * Refresh access token Nhận refresh token từ Authorization header Refresh token là JWT token
     * được trả về khi login/register
     */
    @PostMapping("/refresh")
    public ResponseEntity<ApiResponse<AuthResponse>> refreshToken(
            @RequestHeader("Authorization") String authHeader) {
        log.info("POST /auth/refresh - Refreshing access token");

        // Extract refresh token from Bearer prefix
        // Client gửi refresh token (không phải access token) trong Authorization header
        String refreshToken = authHeader.replace("Bearer ", "");
        AuthResponse response = authService.refreshToken(refreshToken);

        return ResponseEntity.ok(ApiResponse.success("Làm mới token thành công", response));
    }

    /**
     * Logout customer
     */
    @PostMapping("/logout")
    public ResponseEntity<ApiResponse<String>> logout(
            @RequestHeader("Authorization") String authHeader) {
        log.info("POST /auth/logout - Logging out customer");

        // Extract token from Bearer prefix
        String refreshToken = authHeader.replace("Bearer ", "");
        authService.logout(refreshToken);

        return ResponseEntity.ok(ApiResponse.success("Đăng xuất thành công"));
    }

    /**
     * Logout all sessions of current customer
     */
    @PostMapping("/logout-all")
    public ResponseEntity<ApiResponse<String>> logoutAll(
            @RequestHeader("Authorization") String authHeader) {
        log.info("POST /auth/logout-all - Logging out all sessions");

        // Extract token from Bearer prefix
        String refreshToken = authHeader.replace("Bearer ", "");

        // Get customer ID from token
        Integer customerId = tokenProvider.getUserIdFromToken(refreshToken);
        authService.logoutAll(customerId);

        return ResponseEntity.ok(ApiResponse.success("Đã đăng xuất tất cả các phiên đăng nhập"));
    }

    /**
     * Health check endpoint for authentication service
     */
    @GetMapping("/health")
    public ResponseEntity<ApiResponse<String>> health() {
        return ResponseEntity.ok(ApiResponse.success("Dịch vụ xác thực đang hoạt động"));
    }
}

