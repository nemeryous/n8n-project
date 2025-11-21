package com.shop_api.backend.dto;

import java.math.BigDecimal;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO để trả về kết quả validation coupon
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CouponValidationDto {

    @JsonProperty("is_valid")
    private Boolean isValid;

    @JsonProperty("discount_amount")
    private BigDecimal discountAmount;

    @JsonProperty("final_amount")
    private BigDecimal finalAmount;

    @JsonProperty("message")
    private String message;

    /**
     * Tạo response thành công
     *
     * @param discountAmount số tiền được giảm
     * @param finalAmount số tiền cuối cùng sau khi giảm
     * @return CouponValidationDto
     */
    public static CouponValidationDto success(final BigDecimal discountAmount,
            final BigDecimal finalAmount) {
        return new CouponValidationDto(true, discountAmount, finalAmount, "Coupon hợp lệ");
    }

    /**
     * Tạo response thất bại
     *
     * @param message thông báo lỗi
     * @return CouponValidationDto
     */
    public static CouponValidationDto failure(final String message) {
        return new CouponValidationDto(false, BigDecimal.ZERO, BigDecimal.ZERO, message);
    }
}

