package com.shop_api.backend.repository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import com.shop_api.backend.constant.CustomerSegment;
import com.shop_api.backend.entity.Coupon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Repository cho Coupon entity
 */
@Repository
public interface CouponRepository extends JpaRepository<Coupon, Integer> {

    /**
     * Tìm coupon theo code
     *
     * @param code mã coupon
     * @return Optional Coupon
     */
    Optional<Coupon> findByCode(String code);

    /**
     * Kiểm tra code đã tồn tại chưa
     *
     * @param code mã coupon
     * @return true nếu tồn tại
     */
    boolean existsByCode(String code);

    /**
     * Lấy danh sách coupon theo customer segment
     *
     * @param segment phân khúc khách hàng
     * @return danh sách coupon
     */
    @Query("SELECT c FROM Coupon c WHERE c.customerSegment = :segment OR c.customerSegment IS NULL")
    List<Coupon> findByCustomerSegment(@Param("segment") CustomerSegment segment);

    /**
     * Lấy danh sách coupon đang hoạt động và còn hiệu lực
     *
     * @param now thời gian hiện tại
     * @return danh sách coupon
     */
    @Query("SELECT c FROM Coupon c WHERE c.isActive = true "
            + "AND c.validFrom <= :now AND c.validTo >= :now "
            + "AND (c.usageLimit IS NULL OR c.usedCount < c.usageLimit)")
    List<Coupon> findActiveAndValidCoupons(@Param("now") Instant now);

    /**
     * Lấy danh sách coupon đang hoạt động, còn hiệu lực và phù hợp với segment
     *
     * @param segment phân khúc khách hàng
     * @param now thời gian hiện tại
     * @return danh sách coupon
     */
    @Query("SELECT c FROM Coupon c WHERE c.isActive = true "
            + "AND c.validFrom <= :now AND c.validTo >= :now "
            + "AND (c.usageLimit IS NULL OR c.usedCount < c.usageLimit) "
            + "AND (c.customerSegment = :segment OR c.customerSegment IS NULL)")
    List<Coupon> findAvailableCouponsBySegment(@Param("segment") CustomerSegment segment,
            @Param("now") Instant now);
}

