package com.shop_api.backend.entity;

import java.math.BigDecimal;
import java.time.Instant;
import com.shop_api.backend.constant.ApplicableTo;
import com.shop_api.backend.constant.CustomerSegment;
import com.shop_api.backend.constant.DiscountType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "coupons")
public class Coupon {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * Mã coupon (unique)
     */
    @Column(name = "code", nullable = false, unique = true, length = 50)
    private String code;

    /**
     * Loại giảm giá (PERCENTAGE hoặc FIXED_AMOUNT)
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "discount_type", nullable = false, length = 20)
    private DiscountType discountType;

    /**
     * Giá trị giảm giá - Nếu PERCENTAGE: giá trị là phần trăm (ví dụ: 10 = 10%) - Nếu FIXED_AMOUNT:
     * giá trị là số tiền (ví dụ: 50000)
     */
    @Column(name = "discount_value", nullable = false, precision = 10, scale = 2)
    private BigDecimal discountValue;

    /**
     * Giá trị đơn hàng tối thiểu để áp dụng coupon
     */
    @Column(name = "min_order_value", precision = 10, scale = 2)
    private BigDecimal minOrderValue;

    /**
     * Số tiền giảm tối đa (chỉ áp dụng cho PERCENTAGE)
     */
    @Column(name = "max_discount_amount", precision = 10, scale = 2)
    private BigDecimal maxDiscountAmount;

    /**
     * Ngày bắt đầu hiệu lực
     */
    @Column(name = "valid_from", nullable = false)
    private Instant validFrom;

    /**
     * Ngày kết thúc hiệu lực
     */
    @Column(name = "valid_to", nullable = false)
    private Instant validTo;

    /**
     * Giới hạn số lần sử dụng (null = không giới hạn)
     */
    @Column(name = "usage_limit")
    private Integer usageLimit;

    /**
     * Số lần đã sử dụng
     */
    @Column(name = "used_count", nullable = false)
    private Integer usedCount = 0;

    /**
     * Trạng thái hoạt động
     */
    @Column(name = "is_active", nullable = false)
    private Boolean isActive = true;

    /**
     * Phạm vi áp dụng (ALL, SPECIFIC_PRODUCTS, SPECIFIC_CATEGORIES)
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "applicable_to", nullable = false, length = 30)
    private ApplicableTo applicableTo = ApplicableTo.ALL;

    /**
     * Danh sách sản phẩm áp dụng (JSON array, chỉ dùng khi applicableTo = SPECIFIC_PRODUCTS)
     */
    @Column(name = "product_ids", columnDefinition = "TEXT")
    private String productIds;

    /**
     * Danh sách danh mục áp dụng (JSON array, chỉ dùng khi applicableTo = SPECIFIC_CATEGORIES)
     */
    @Column(name = "category_names", columnDefinition = "TEXT")
    private String categoryNames;

    /**
     * Phân khúc khách hàng áp dụng (ALL, NEW, VIP, etc.)
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "customer_segment", length = 20)
    private CustomerSegment customerSegment;

    /**
     * Mô tả coupon
     */
    @Column(name = "description", length = 500)
    private String description;

    /**
     * Thời gian tạo
     */
    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    /**
     * Thời gian cập nhật
     */
    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;
}

