package com.shop_api.backend.dto;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.shop_api.backend.constant.ApplicableTo;
import com.shop_api.backend.constant.CustomerSegment;
import com.shop_api.backend.constant.DiscountType;
import com.shop_api.backend.entity.Coupon;
import lombok.Data;

/**
 * DTO để trả về thông tin coupon
 */
@Data
public class CouponDto {

    @JsonProperty("id")
    private Integer id;

    @JsonProperty("code")
    private String code;

    @JsonProperty("discount_type")
    private DiscountType discountType;

    @JsonProperty("discount_value")
    private BigDecimal discountValue;

    @JsonProperty("min_order_value")
    private BigDecimal minOrderValue;

    @JsonProperty("max_discount_amount")
    private BigDecimal maxDiscountAmount;

    @JsonProperty("valid_from")
    private Instant validFrom;

    @JsonProperty("valid_to")
    private Instant validTo;

    @JsonProperty("usage_limit")
    private Integer usageLimit;

    @JsonProperty("used_count")
    private Integer usedCount;

    @JsonProperty("is_active")
    private Boolean isActive;

    @JsonProperty("applicable_to")
    private ApplicableTo applicableTo;

    @JsonProperty("product_ids")
    private List<Integer> productIds;

    @JsonProperty("category_names")
    private List<String> categoryNames;

    @JsonProperty("customer_segment")
    private CustomerSegment customerSegment;

    @JsonProperty("description")
    private String description;

    @JsonProperty("created_at")
    private Instant createdAt;

    @JsonProperty("updated_at")
    private Instant updatedAt;

    /**
     * Chuyển đổi Entity thành DTO
     *
     * @param coupon entity
     * @return CouponDto
     */
    public static CouponDto fromEntity(final Coupon coupon) {
        final CouponDto dto = new CouponDto();
        dto.setId(coupon.getId());
        dto.setCode(coupon.getCode());
        dto.setDiscountType(coupon.getDiscountType());
        dto.setDiscountValue(coupon.getDiscountValue());
        dto.setMinOrderValue(coupon.getMinOrderValue());
        dto.setMaxDiscountAmount(coupon.getMaxDiscountAmount());
        dto.setValidFrom(coupon.getValidFrom());
        dto.setValidTo(coupon.getValidTo());
        dto.setUsageLimit(coupon.getUsageLimit());
        dto.setUsedCount(coupon.getUsedCount());
        dto.setIsActive(coupon.getIsActive());
        dto.setApplicableTo(coupon.getApplicableTo());
        dto.setProductIds(parseJsonToList(coupon.getProductIds(), Integer::parseInt));
        dto.setCategoryNames(parseJsonToList(coupon.getCategoryNames(), String::toString));
        dto.setCustomerSegment(coupon.getCustomerSegment());
        dto.setDescription(coupon.getDescription());
        dto.setCreatedAt(coupon.getCreatedAt());
        dto.setUpdatedAt(coupon.getUpdatedAt());

        return dto;
    }

    /**
     * Parse JSON string thành List
     *
     * @param jsonString JSON string
     * @param mapper function để map từ String sang type T
     * @param <T> type của phần tử trong list
     * @return List hoặc null
     */
    private static <T> List<T> parseJsonToList(final String jsonString,
            final java.util.function.Function<String, T> mapper) {
        if (jsonString == null || jsonString.trim().isEmpty()) {
            return null;
        }
        return Arrays.stream(jsonString.split(",")).map(String::trim).filter(s -> !s.isEmpty())
                .map(mapper).collect(Collectors.toList());
    }

    /**
     * Chuyển đổi danh sách Entity thành DTO
     *
     * @param coupons danh sách coupon
     * @return danh sách CouponDto
     */
    public static List<CouponDto> fromEntities(final List<Coupon> coupons) {
        return coupons.stream().map(CouponDto::fromEntity).collect(Collectors.toList());
    }
}

