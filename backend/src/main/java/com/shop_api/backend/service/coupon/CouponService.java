package com.shop_api.backend.service.coupon;

import java.math.BigDecimal;
import java.util.List;
import com.shop_api.backend.constant.CustomerSegment;
import com.shop_api.backend.dto.CouponDto;
import com.shop_api.backend.dto.CouponValidationDto;
import com.shop_api.backend.dto.CreateCouponDto;
import com.shop_api.backend.dto.UpdateCouponDto;

/**
 * Service interface cho Coupon management
 */
public interface CouponService {

    /**
     * Tạo coupon mới
     *
     * @param createCouponDto DTO chứa thông tin coupon
     * @return CouponDto đã tạo
     */
    CouponDto createCoupon(CreateCouponDto createCouponDto);

    /**
     * Lấy coupon theo ID
     *
     * @param id ID của coupon
     * @return CouponDto
     */
    CouponDto getCouponById(Integer id);

    /**
     * Lấy coupon theo code
     *
     * @param code mã coupon
     * @return CouponDto
     */
    CouponDto getCouponByCode(String code);

    /**
     * Lấy tất cả coupons
     *
     * @return danh sách CouponDto
     */
    List<CouponDto> getAllCoupons();

    /**
     * Cập nhật coupon
     *
     * @param id ID của coupon
     * @param updateCouponDto DTO chứa thông tin cập nhật
     * @return CouponDto đã cập nhật
     */
    CouponDto updateCoupon(Integer id, UpdateCouponDto updateCouponDto);

    /**
     * Xóa coupon
     *
     * @param id ID của coupon
     */
    void deleteCoupon(Integer id);

    /**
     * Validate coupon và tính toán discount
     *
     * @param code mã coupon
     * @param customerId ID khách hàng
     * @param orderAmount giá trị đơn hàng
     * @param productIds danh sách ID sản phẩm trong đơn hàng
     * @param categories danh sách category trong đơn hàng
     * @return CouponValidationDto chứa kết quả validation
     */
    CouponValidationDto validateCoupon(String code, Integer customerId, BigDecimal orderAmount,
            List<Integer> productIds, List<String> categories);

    /**
     * Áp dụng coupon vào đơn hàng (tăng usedCount)
     *
     * @param couponId ID của coupon
     */
    void applyCoupon(Integer couponId);

    /**
     * Lấy danh sách coupon available cho customer
     *
     * @param customerSegment phân khúc khách hàng
     * @return danh sách CouponDto
     */
    List<CouponDto> getAvailableCouponsBySegment(CustomerSegment customerSegment);
}

