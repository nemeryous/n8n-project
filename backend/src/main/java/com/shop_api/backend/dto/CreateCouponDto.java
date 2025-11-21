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
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * DTO để tạo coupon mới
 */
@Data
public class CreateCouponDto {

    @NotBlank(message = "Mã coupon không được để trống")
    @Size(max = 50, message = "Mã coupon không được vượt quá 50 ký tự")
    @JsonProperty("code")
    private String code;

    @NotNull(message = "Loại giảm giá không được để trống")
    @JsonProperty("discount_type")
    private DiscountType discountType;

    @NotNull(message = "Giá trị giảm giá không được để trống")
    @DecimalMin(value = "0.01", message = "Giá trị giảm giá phải lớn hơn 0")
    @JsonProperty("discount_value")
    private BigDecimal discountValue;

    @DecimalMin(value = "0", message = "Giá trị đơn hàng tối thiểu phải >= 0")
    @JsonProperty("min_order_value")
    private BigDecimal minOrderValue;

    @DecimalMin(value = "0", message = "Số tiền giảm tối đa phải >= 0")
    @JsonProperty("max_discount_amount")
    private BigDecimal maxDiscountAmount;

    @NotNull(message = "Ngày bắt đầu hiệu lực không được để trống")
    @JsonProperty("valid_from")
    private Instant validFrom;

    @NotNull(message = "Ngày kết thúc hiệu lực không được để trống")
    @JsonProperty("valid_to")
    private Instant validTo;

    @Min(value = 1, message = "Giới hạn sử dụng phải >= 1")
    @JsonProperty("usage_limit")
    private Integer usageLimit;

    @JsonProperty("applicable_to")
    private ApplicableTo applicableTo = ApplicableTo.ALL;

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
     * Chuyển đổi DTO thành Entity
     *
     * @return Coupon entity
     */
    public Coupon toEntity() {
        final Coupon coupon = new Coupon();
        coupon.setCode(this.code);
        coupon.setDiscountType(this.discountType);
        coupon.setDiscountValue(this.discountValue);
        coupon.setMinOrderValue(this.minOrderValue);
        coupon.setMaxDiscountAmount(this.maxDiscountAmount);
        coupon.setValidFrom(this.validFrom);
        coupon.setValidTo(this.validTo);
        coupon.setUsageLimit(this.usageLimit);
        coupon.setUsedCount(0);
        coupon.setIsActive(true);
        coupon.setApplicableTo(this.applicableTo);
        coupon.setProductIds(convertListToJson(this.productIds));
        coupon.setCategoryNames(convertListToJson(this.categoryNames));
        coupon.setCustomerSegment(this.customerSegment);
        coupon.setDescription(this.description);
        coupon.setCreatedAt(Instant.now());
        coupon.setUpdatedAt(Instant.now());

        return coupon;
    }

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
        // Sử dụng Jackson ObjectMapper hoặc đơn giản là join
        return String.join(",", list.stream().map(Object::toString).toList());
    }
}

