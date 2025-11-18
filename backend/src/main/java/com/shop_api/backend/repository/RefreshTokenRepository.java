package com.shop_api.backend.repository;

import java.time.Instant;
import java.util.Optional;
import com.shop_api.backend.entity.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Repository for RefreshToken entity
 */
@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Integer> {

    /**
     * Tìm refresh token theo token string
     */
    Optional<RefreshToken> findByToken(String token);

    /**
     * Tìm refresh token hợp lệ (chưa revoke và chưa hết hạn) theo customer ID
     */
    @Query("SELECT rt FROM RefreshToken rt WHERE rt.customerId = :customerId "
            + "AND rt.isRevoked = false AND rt.expiresAt > :now")
    Optional<RefreshToken> findValidTokenByCustomerId(@Param("customerId") Integer customerId,
            @Param("now") Instant now);

    /**
     * Revoke tất cả refresh tokens của một customer
     */
    @Modifying
    @Query("UPDATE RefreshToken rt SET rt.isRevoked = true, rt.revokedAt = :now "
            + "WHERE rt.customerId = :customerId AND rt.isRevoked = false")
    int revokeAllByCustomerId(@Param("customerId") Integer customerId, @Param("now") Instant now);

    /**
     * Xóa các refresh token đã hết hạn hoặc đã revoke
     */
    @Modifying
    @Query("DELETE FROM RefreshToken rt WHERE rt.expiresAt < :now OR rt.isRevoked = true")
    int deleteExpiredOrRevokedTokens(@Param("now") Instant now);

    /**
     * Đếm số refresh tokens đang active của một customer
     */
    @Query("SELECT COUNT(rt) FROM RefreshToken rt WHERE rt.customerId = :customerId "
            + "AND rt.isRevoked = false AND rt.expiresAt > :now")
    long countActiveTokensByCustomerId(@Param("customerId") Integer customerId,
            @Param("now") Instant now);
}

