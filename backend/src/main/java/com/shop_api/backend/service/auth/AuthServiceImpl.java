package com.shop_api.backend.service.auth;

import java.time.Instant;
import com.shop_api.backend.constant.CustomerSegment;
import com.shop_api.backend.constant.Role;
import com.shop_api.backend.dto.request.LoginRequest;
import com.shop_api.backend.dto.request.RegisterRequest;
import com.shop_api.backend.dto.response.AuthResponse;
import com.shop_api.backend.entity.Customer;
import com.shop_api.backend.entity.RefreshToken;
import com.shop_api.backend.exception.BadRequestException;
import com.shop_api.backend.exception.ConflictException;
import com.shop_api.backend.exception.UnauthorizedException;
import com.shop_api.backend.repository.CustomerRepository;
import com.shop_api.backend.repository.RefreshTokenRepository;
import com.shop_api.backend.security.JwtProperties;
import com.shop_api.backend.security.JwtTokenProvider;
import com.shop_api.backend.security.UserPrincipal;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Authentication service implementation Handles user registration, login, and token refresh
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final CustomerRepository customerRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider tokenProvider;
    private final JwtProperties jwtProperties;
    private final AuthenticationManager authenticationManager;

    @Override
    @Transactional
    public AuthResponse register(RegisterRequest request) {
        log.info("Registering new customer with email: {}", request.getEmail());

        // Check if email already exists
        if (customerRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new ConflictException("Địa chỉ email đã được sử dụng");
        }

        // Create new customer
        Customer customer = new Customer();
        customer.setName(request.getName());
        customer.setEmail(request.getEmail());
        customer.setHashPasswords(passwordEncoder.encode(request.getPassword()));
        customer.setPhone(request.getPhone());
        customer.setAddress(request.getAddress());
        customer.setCustomerSegment(CustomerSegment.NEW);

        customer = customerRepository.save(customer);
        log.info("Customer registered successfully with ID: {}", customer.getId());

        // Generate tokens
        String roleName = customer.getRole() != null ? customer.getRole().name() : Role.USER.name();
        String accessToken = tokenProvider.generateTokenFromUserId(customer.getId(),
                customer.getEmail(), customer.getName(), roleName);
        String refreshTokenString = tokenProvider.generateRefreshToken(customer.getId());

        // Save refresh token to database
        saveRefreshToken(customer.getId(), refreshTokenString, null, null);

        return buildAuthResponse(customer, accessToken, refreshTokenString);
    }

    @Override
    @Transactional
    public AuthResponse login(LoginRequest request) {
        log.info("Authenticating customer with email: {}", request.getEmail());

        // Authenticate user
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));

        // Generate tokens
        String accessToken = tokenProvider.generateToken(authentication);
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        String refreshTokenString = tokenProvider.generateRefreshToken(userPrincipal.getId());

        // Get customer details
        Customer customer = customerRepository.findById(userPrincipal.getId())
                .orElseThrow(() -> new UnauthorizedException("Không tìm thấy khách hàng"));

        // Save refresh token to database
        saveRefreshToken(customer.getId(), refreshTokenString, null, null);

        log.info("Customer logged in successfully with ID: {}", customer.getId());
        return buildAuthResponse(customer, accessToken, refreshTokenString);
    }

    @Override
    @Transactional
    public AuthResponse refreshToken(String refreshTokenString) {
        try {
            log.info("Refreshing access token");

            // Validate refresh token structure (signature and format only)
            // Expiration will be checked from database
            tokenProvider.validateRefreshTokenStructure(refreshTokenString);

            // Check if token exists in database and is valid
            RefreshToken refreshToken = refreshTokenRepository.findByToken(refreshTokenString)
                    .orElseThrow(() -> new UnauthorizedException("Refresh token không tồn tại"));

            // Check if token is revoked
            if (refreshToken.getIsRevoked()) {
                throw new UnauthorizedException("Refresh token đã bị thu hồi");
            }

            // Check if token is expired
            if (refreshToken.getExpiresAt().isBefore(Instant.now())) {
                throw new UnauthorizedException("Refresh token đã hết hạn");
            }

            // Get customer
            Customer customer = customerRepository.findById(refreshToken.getCustomerId())
                    .orElseThrow(() -> new UnauthorizedException("Không tìm thấy khách hàng"));

            // Revoke old refresh token
            refreshToken.setIsRevoked(true);
            refreshToken.setRevokedAt(Instant.now());
            refreshTokenRepository.save(refreshToken);

            // Generate new tokens
            String roleName =
                    customer.getRole() != null ? customer.getRole().name() : Role.USER.name();
            String newAccessToken = tokenProvider.generateTokenFromUserId(customer.getId(),
                    customer.getEmail(), customer.getName(), roleName);
            String newRefreshTokenString = tokenProvider.generateRefreshToken(customer.getId());

            // Save new refresh token to database
            saveRefreshToken(customer.getId(), newRefreshTokenString, null, null);

            log.info("Access token refreshed successfully for customer ID: {}", customer.getId());
            return buildAuthResponse(customer, newAccessToken, newRefreshTokenString);

        } catch (UnauthorizedException ex) {
            throw ex;
        } catch (Exception ex) {
            log.error("Error refreshing token: {}", ex.getMessage());
            throw new BadRequestException("Refresh token không hợp lệ hoặc đã hết hạn");
        }
    }

    @Override
    @Transactional
    public void logout(String refreshTokenString) {
        log.info("Logging out customer");

        RefreshToken refreshToken = refreshTokenRepository.findByToken(refreshTokenString)
                .orElseThrow(() -> new BadRequestException("Refresh token không tồn tại"));

        if (refreshToken.getIsRevoked()) {
            log.warn("Refresh token already revoked");
            return;
        }

        refreshToken.setIsRevoked(true);
        refreshToken.setRevokedAt(Instant.now());
        refreshTokenRepository.save(refreshToken);

        log.info("Customer logged out successfully. Token revoked for customer ID: {}",
                refreshToken.getCustomerId());
    }

    @Override
    @Transactional
    public void logoutAll(Integer customerId) {
        log.info("Logging out all sessions for customer ID: {}", customerId);

        int revokedCount = refreshTokenRepository.revokeAllByCustomerId(customerId, Instant.now());
        log.info("Revoked {} refresh tokens for customer ID: {}", revokedCount, customerId);
    }

    /**
     * Helper method to save refresh token to database Follows DRY principle
     */
    private void saveRefreshToken(Integer customerId, String token, String deviceInfo,
            String ipAddress) {
        Instant now = Instant.now();
        Instant expiresAt = now.plusMillis(jwtProperties.getRefreshExpirationMs());

        RefreshToken refreshToken = RefreshToken.builder().token(token).customerId(customerId)
                .expiresAt(expiresAt).createdAt(now).isRevoked(false).deviceInfo(deviceInfo)
                .ipAddress(ipAddress).build();

        refreshTokenRepository.save(refreshToken);
        log.debug("Refresh token saved for customer ID: {}", customerId);
    }

    /**
     * Helper method to build AuthResponse Follows DRY principle
     */
    private AuthResponse buildAuthResponse(Customer customer, String accessToken,
            String refreshToken) {
        AuthResponse.UserInfo userInfo = AuthResponse.UserInfo.builder().id(customer.getId())
                .name(customer.getName()).email(customer.getEmail()).phone(customer.getPhone())
                .address(customer.getAddress())
                .role(customer.getRole() != null ? customer.getRole() : Role.USER).build();

        return AuthResponse.builder().accessToken(accessToken).refreshToken(refreshToken)
                .tokenType("Bearer").user(userInfo).build();
    }
}

