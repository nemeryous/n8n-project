package com.shop_api.backend.dto;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.shop_api.backend.constant.ApplicableTo;
import com.shop_api.backend.constant.CustomerSegment;
import com.shop_api.backend.constant.DiscountType;
import com.shop_api.backend.entity.Coupon;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * DTO để cập nhật coupon
 */
@Data
public class UpdateCouponDto {

    @Size(max = 50, message = "Mã coupon không được vượt quá 50 ký tự")
    @JsonProperty("code")
    private String code;

    @JsonProperty("discount_type")
    private DiscountType discountType;

    @DecimalMin(value = "0.01", message = "Giá trị giảm giá phải lớn hơn 0")
    @JsonProperty("discount_value")
    private BigDecimal discountValue;

    @DecimalMin(value = "0", message = "Giá trị đơn hàng tối thiểu phải >= 0")
    @JsonProperty("min_order_value")
    private BigDecimal minOrderValue;

    @DecimalMin(value = "0", message = "Số tiền giảm tối đa phải >= 0")
    @JsonProperty("max_discount_amount")
    private BigDecimal maxDiscountAmount;

    @JsonProperty("valid_from")
    private Instant validFrom;

    @JsonProperty("valid_to")
    private Instant validTo;

    @Min(value = 1, message = "Giới hạn sử dụng phải >= 1")
    @JsonProperty("usage_limit")
    private Integer usageLimit;

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

    @Size(max = 500, message = "Mô tả không được vượt quá 500 ký tự")
    @JsonProperty("description")
    private String description;

    /**
     * Chuyển đổi List thành JSON string
     *
     * @param list danh sách cần chuyển đổi
     * @return JSON string hoặc null
     */
    private String convertListToJson(final List<?> list) {
        if (list == null || list.isEmpty()) {
            return null;
        }
        return String.join(",", list.stream().map(Object::toString).toList());
    }

    /**
     * Cập nhật entity với dữ liệu từ DTO
     *
     * @param coupon entity cần cập nhật
     */
    public void updateEntity(final Coupon coupon) {
        if (this.code != null) {
            coupon.setCode(this.code);
        }
        if (this.discountType != null) {
            coupon.setDiscountType(this.discountType);
        }
        if (this.discountValue != null) {
            coupon.setDiscountValue(this.discountValue);
        }
        if (this.minOrderValue != null) {
            coupon.setMinOrderValue(this.minOrderValue);
        }
        if (this.maxDiscountAmount != null) {
            coupon.setMaxDiscountAmount(this.maxDiscountAmount);
        }
        if (this.validFrom != null) {
            coupon.setValidFrom(this.validFrom);
        }
        if (this.validTo != null) {
            coupon.setValidTo(this.validTo);
        }
        if (this.usageLimit != null) {
            coupon.setUsageLimit(this.usageLimit);
        }
        if (this.isActive != null) {
            coupon.setIsActive(this.isActive);
        }
        if (this.applicableTo != null) {
            coupon.setApplicableTo(this.applicableTo);
        }
        if (this.productIds != null) {
            coupon.setProductIds(convertListToJson(this.productIds));
        }
        if (this.categoryNames != null) {
            coupon.setCategoryNames(convertListToJson(this.categoryNames));
        }
        if (this.customerSegment != null) {
            coupon.setCustomerSegment(this.customerSegment);
        }
        if (this.description != null) {
            coupon.setDescription(this.description);
        }
        coupon.setUpdatedAt(Instant.now());
    }
}

