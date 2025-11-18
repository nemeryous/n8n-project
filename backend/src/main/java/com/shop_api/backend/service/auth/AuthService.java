package com.shop_api.backend.service.auth;

import com.shop_api.backend.dto.request.LoginRequest;
import com.shop_api.backend.dto.request.RegisterRequest;
import com.shop_api.backend.dto.response.AuthResponse;

/**
 * Authentication service interface Follows Interface Segregation Principle
 */
public interface AuthService {

    /**
     * Register a new customer
     */
    AuthResponse register(RegisterRequest request);

    /**
     * Authenticate customer and generate tokens
     */
    AuthResponse login(LoginRequest request);

    /**
     * Refresh access token using refresh token
     */
    AuthResponse refreshToken(String refreshToken);

    /**
     * Logout customer by revoking refresh token
     */
    void logout(String refreshToken);

    /**
     * Logout all sessions of a customer
     */
    void logoutAll(Integer customerId);
}

