package com.shop_api.backend.controller;

import com.shop_api.backend.dto.request.LoginRequest;
import com.shop_api.backend.dto.request.RegisterRequest;
import com.shop_api.backend.dto.response.ApiResponse;
import com.shop_api.backend.dto.response.AuthResponse;
import com.shop_api.backend.dto.response.ErrorResponse;
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
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Authentication",
        description = "API quản lý xác thực: đăng ký, đăng nhập, refresh token, logout")
public class AuthController {

    private final AuthService authService;
    private final JwtTokenProvider tokenProvider;

    /**
     * Register a new customer
     */
    @Operation(summary = "Đăng ký tài khoản mới",
            description = "Tạo tài khoản khách hàng mới và nhận access token, refresh token")
    @ApiResponses(
            value = {
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201",
                            description = "Đăng ký thành công",
                            content = @Content(
                                    schema = @Schema(implementation = AuthResponse.class))),
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400",
                            description = "Dữ liệu không hợp lệ",
                            content = @Content(
                                    schema = @Schema(implementation = ErrorResponse.class))),
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "409",
                            description = "Email đã được sử dụng",
                            content = @Content(
                                    schema = @Schema(implementation = ErrorResponse.class)))})
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
    @Operation(summary = "Đăng nhập",
            description = "Xác thực khách hàng và nhận access token, refresh token")
    @ApiResponses(
            value = {
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200",
                            description = "Đăng nhập thành công",
                            content = @Content(
                                    schema = @Schema(implementation = AuthResponse.class))),
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401",
                            description = "Email hoặc mật khẩu không đúng",
                            content = @Content(
                                    schema = @Schema(implementation = ErrorResponse.class)))})
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
    @Operation(summary = "Làm mới access token",
            description = "Sử dụng refresh token để lấy access token và refresh token mới. Refresh token cũ sẽ bị thu hồi.")
    @ApiResponses(
            value = {
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200",
                            description = "Làm mới token thành công",
                            content = @Content(
                                    schema = @Schema(implementation = AuthResponse.class))),
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401",
                            description = "Refresh token không hợp lệ hoặc đã hết hạn",
                            content = @Content(
                                    schema = @Schema(implementation = ErrorResponse.class)))})
    @PostMapping("/refresh")
    public ResponseEntity<ApiResponse<AuthResponse>> refreshToken(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Refresh token trong Authorization header",
                    required = true) @RequestHeader("Authorization") String authHeader) {
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
    @Operation(summary = "Đăng xuất",
            description = "Thu hồi refresh token hiện tại. Cần gửi refresh token trong Authorization header.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200",
                    description = "Đăng xuất thành công"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400",
                    description = "Refresh token không tồn tại",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))})
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
    @Operation(summary = "Đăng xuất tất cả phiên đăng nhập",
            description = "Thu hồi tất cả refresh tokens của khách hàng hiện tại. Cần gửi refresh token trong Authorization header.")
    @ApiResponses(
            value = {@io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200",
                    description = "Đã đăng xuất tất cả các phiên đăng nhập")})
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
    @Operation(summary = "Health check", description = "Kiểm tra trạng thái dịch vụ xác thực")
    @GetMapping("/health")
    public ResponseEntity<ApiResponse<String>> health() {
        return ResponseEntity.ok(ApiResponse.success("Dịch vụ xác thực đang hoạt động"));
    }
}

