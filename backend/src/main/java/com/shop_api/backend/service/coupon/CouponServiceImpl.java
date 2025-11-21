package com.shop_api.backend.service.coupon;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Instant;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import com.shop_api.backend.constant.ApplicableTo;
import com.shop_api.backend.constant.CustomerSegment;
import com.shop_api.backend.constant.DiscountType;
import com.shop_api.backend.dto.CouponDto;
import com.shop_api.backend.dto.CouponValidationDto;
import com.shop_api.backend.dto.CreateCouponDto;
import com.shop_api.backend.dto.UpdateCouponDto;
import com.shop_api.backend.entity.Coupon;
import com.shop_api.backend.entity.Customer;
import com.shop_api.backend.exception.BadRequestException;
import com.shop_api.backend.exception.ConflictException;
import com.shop_api.backend.exception.ResourceNotFoundException;
import com.shop_api.backend.repository.CouponRepository;
import com.shop_api.backend.repository.CustomerRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Service implementation cho Coupon management Tuân thủ SOLID principles: - Single Responsibility:
 * Chỉ quản lý coupon logic - Open/Closed: Dễ mở rộng, khó sửa đổi - Dependency Inversion: Phụ thuộc
 * vào abstraction (repository)
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class CouponServiceImpl implements CouponService {

    private final CouponRepository couponRepository;
    private final CustomerRepository customerRepository;

    private static final BigDecimal ONE_HUNDRED = BigDecimal.valueOf(100);
    private static final int SCALE = 2;
    private static final RoundingMode ROUNDING_MODE = RoundingMode.HALF_UP;

    @Override
    @Transactional
    public CouponDto createCoupon(final CreateCouponDto createCouponDto) {
        log.info("Creating new coupon with code: {}", createCouponDto.getCode());

        // Validate code uniqueness
        validateCodeUniqueness(createCouponDto.getCode());

        // Validate business rules
        validateCouponBusinessRules(createCouponDto);

        // Convert DTO to Entity
        final Coupon coupon = createCouponDto.toEntity();
        final Coupon savedCoupon = couponRepository.save(coupon);

        log.info("Coupon created successfully with ID: {}", savedCoupon.getId());
        return CouponDto.fromEntity(savedCoupon);
    }

    @Override
    public CouponDto getCouponById(final Integer id) {
        log.debug("Fetching coupon by ID: {}", id);
        final Coupon coupon = couponRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Coupon", "id", id));
        return CouponDto.fromEntity(coupon);
    }

    @Override
    public CouponDto getCouponByCode(final String code) {
        log.debug("Fetching coupon by code: {}", code);
        final Coupon coupon = couponRepository.findByCode(code)
                .orElseThrow(() -> new ResourceNotFoundException("Coupon", "code", code));
        return CouponDto.fromEntity(coupon);
    }

    @Override
    public List<CouponDto> getAllCoupons() {
        log.debug("Fetching all coupons");
        return CouponDto.fromEntities(couponRepository.findAll());
    }

    @Override
    @Transactional
    public CouponDto updateCoupon(final Integer id, final UpdateCouponDto updateCouponDto) {
        log.info("Updating coupon with ID: {}", id);

        final Coupon coupon = couponRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Coupon", "id", id));

        // Validate code uniqueness if code is being updated
        if (updateCouponDto.getCode() != null
                && !updateCouponDto.getCode().equals(coupon.getCode())) {
            validateCodeUniqueness(updateCouponDto.getCode());
        }

        // Update entity
        updateCouponDto.updateEntity(coupon);
        final Coupon updatedCoupon = couponRepository.save(coupon);

        log.info("Coupon updated successfully with ID: {}", updatedCoupon.getId());
        return CouponDto.fromEntity(updatedCoupon);
    }

    @Override
    @Transactional
    public void deleteCoupon(final Integer id) {
        log.info("Deleting coupon with ID: {}", id);

        if (!couponRepository.existsById(id)) {
            throw new ResourceNotFoundException("Coupon", "id", id);
        }

        couponRepository.deleteById(id);
        log.info("Coupon deleted successfully with ID: {}", id);
    }

    @Override
    @Transactional(readOnly = true)
    public CouponValidationDto validateCoupon(final String code, final Integer customerId,
            final BigDecimal orderAmount, final List<Integer> productIds,
            final List<String> categories) {
        log.debug("Validating coupon: {} for customer: {} with order amount: {}", code, customerId,
                orderAmount);

        // Find coupon
        final Coupon coupon = couponRepository.findByCode(code)
                .orElseThrow(() -> new BadRequestException("Mã coupon không tồn tại"));

        // Validate coupon status
        validateCouponStatus(coupon);

        // Validate customer segment
        validateCustomerSegment(coupon, customerId);

        // Validate order amount
        validateOrderAmount(coupon, orderAmount);

        // Validate applicable to
        validateApplicableTo(coupon, productIds, categories);

        // Calculate discount
        final BigDecimal discountAmount = calculateDiscount(coupon, orderAmount);
        final BigDecimal finalAmount = orderAmount.subtract(discountAmount);

        log.info("Coupon validated successfully. Discount: {}, Final amount: {}", discountAmount,
                finalAmount);
        return CouponValidationDto.success(discountAmount, finalAmount);
    }

    @Override
    @Transactional
    public void applyCoupon(final Integer couponId) {
        log.info("Applying coupon with ID: {}", couponId);

        final Coupon coupon = couponRepository.findById(couponId)
                .orElseThrow(() -> new ResourceNotFoundException("Coupon", "id", couponId));

        // Increment usage count
        coupon.setUsedCount(coupon.getUsedCount() + 1);
        coupon.setUpdatedAt(Instant.now());
        couponRepository.save(coupon);

        log.info("Coupon applied successfully. New usage count: {}", coupon.getUsedCount());
    }

    @Override
    public List<CouponDto> getAvailableCouponsBySegment(final CustomerSegment customerSegment) {
        log.debug("Fetching available coupons for segment: {}", customerSegment);
        final Instant now = Instant.now();
        final List<Coupon> coupons =
                couponRepository.findAvailableCouponsBySegment(customerSegment, now);
        return CouponDto.fromEntities(coupons);
    }

    /**
     * Validate code uniqueness
     *
     * @param code mã coupon
     */
    private void validateCodeUniqueness(final String code) {
        if (couponRepository.existsByCode(code)) {
            throw new ConflictException("Mã coupon đã tồn tại: " + code);
        }
    }

    /**
     * Validate business rules khi tạo coupon
     *
     * @param createCouponDto DTO chứa thông tin coupon
     */
    private void validateCouponBusinessRules(final CreateCouponDto createCouponDto) {
        // Validate date range
        if (createCouponDto.getValidFrom().isAfter(createCouponDto.getValidTo())) {
            throw new BadRequestException("Ngày bắt đầu phải trước ngày kết thúc");
        }

        // Validate discount value
        if (createCouponDto.getDiscountType() == DiscountType.PERCENTAGE) {
            if (createCouponDto.getDiscountValue().compareTo(ONE_HUNDRED) > 0) {
                throw new BadRequestException("Giảm giá theo phần trăm không được vượt quá 100%");
            }
        }

        // Validate applicable to
        if (createCouponDto.getApplicableTo() == ApplicableTo.SPECIFIC_PRODUCTS) {
            if (createCouponDto.getProductIds() == null
                    || createCouponDto.getProductIds().isEmpty()) {
                throw new BadRequestException(
                        "Phải chỉ định danh sách sản phẩm khi applicableTo = SPECIFIC_PRODUCTS");
            }
        }

        if (createCouponDto.getApplicableTo() == ApplicableTo.SPECIFIC_CATEGORIES) {
            if (createCouponDto.getCategoryNames() == null
                    || createCouponDto.getCategoryNames().isEmpty()) {
                throw new BadRequestException(
                        "Phải chỉ định danh sách danh mục khi applicableTo = SPECIFIC_CATEGORIES");
            }
        }
    }

    /**
     * Validate coupon status (active, valid date, usage limit)
     *
     * @param coupon coupon cần validate
     */
    private void validateCouponStatus(final Coupon coupon) {
        if (!coupon.getIsActive()) {
            throw new BadRequestException("Coupon không còn hoạt động");
        }

        final Instant now = Instant.now();
        if (now.isBefore(coupon.getValidFrom())) {
            throw new BadRequestException("Coupon chưa có hiệu lực");
        }

        if (now.isAfter(coupon.getValidTo())) {
            throw new BadRequestException("Coupon đã hết hạn");
        }

        if (coupon.getUsageLimit() != null && coupon.getUsedCount() >= coupon.getUsageLimit()) {
            throw new BadRequestException("Coupon đã đạt giới hạn sử dụng");
        }
    }

    /**
     * Validate customer segment
     *
     * @param coupon coupon
     * @param customerId ID khách hàng
     */
    private void validateCustomerSegment(final Coupon coupon, final Integer customerId) {
        if (coupon.getCustomerSegment() == null) {
            return; // Áp dụng cho tất cả
        }

        final Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new ResourceNotFoundException("Khách hàng", "id", customerId));

        if (customer.getCustomerSegment() != coupon.getCustomerSegment()) {
            throw new BadRequestException(String.format("Coupon chỉ áp dụng cho phân khúc: %s",
                    coupon.getCustomerSegment()));
        }
    }

    /**
     * Validate order amount
     *
     * @param coupon coupon
     * @param orderAmount giá trị đơn hàng
     */
    private void validateOrderAmount(final Coupon coupon, final BigDecimal orderAmount) {
        if (coupon.getMinOrderValue() != null
                && orderAmount.compareTo(coupon.getMinOrderValue()) < 0) {
            throw new BadRequestException(
                    String.format("Giá trị đơn hàng tối thiểu để áp dụng coupon là: %s",
                            coupon.getMinOrderValue()));
        }
    }

    /**
     * Validate applicable to (products/categories)
     *
     * @param coupon coupon
     * @param productIds danh sách ID sản phẩm
     * @param categories danh sách category
     */
    private void validateApplicableTo(final Coupon coupon, final List<Integer> productIds,
            final List<String> categories) {
        if (coupon.getApplicableTo() == ApplicableTo.ALL) {
            return;
        }

        if (coupon.getApplicableTo() == ApplicableTo.SPECIFIC_PRODUCTS) {
            final List<Integer> applicableProductIds = parseProductIds(coupon.getProductIds());
            if (productIds == null || productIds.isEmpty()
                    || !productIds.stream().anyMatch(applicableProductIds::contains)) {
                throw new BadRequestException("Coupon không áp dụng cho sản phẩm trong đơn hàng");
            }
        }

        if (coupon.getApplicableTo() == ApplicableTo.SPECIFIC_CATEGORIES) {
            final List<String> applicableCategories = parseCategoryNames(coupon.getCategoryNames());
            if (categories == null || categories.isEmpty()
                    || !categories.stream().anyMatch(applicableCategories::contains)) {
                throw new BadRequestException("Coupon không áp dụng cho danh mục trong đơn hàng");
            }
        }
    }

    /**
     * Tính toán số tiền được giảm
     *
     * @param coupon coupon
     * @param orderAmount giá trị đơn hàng
     * @return số tiền được giảm
     */
    private BigDecimal calculateDiscount(final Coupon coupon, final BigDecimal orderAmount) {
        BigDecimal discount;

        if (coupon.getDiscountType() == DiscountType.PERCENTAGE) {
            // Tính theo phần trăm
            discount = orderAmount.multiply(coupon.getDiscountValue()).divide(ONE_HUNDRED, SCALE,
                    ROUNDING_MODE);

            // Áp dụng max discount amount nếu có
            if (coupon.getMaxDiscountAmount() != null
                    && discount.compareTo(coupon.getMaxDiscountAmount()) > 0) {
                discount = coupon.getMaxDiscountAmount();
            }
        } else {
            // Tính theo số tiền cố định
            discount = coupon.getDiscountValue();
        }

        // Đảm bảo discount không vượt quá order amount
        if (discount.compareTo(orderAmount) > 0) {
            discount = orderAmount;
        }

        return discount.setScale(SCALE, ROUNDING_MODE);
    }

    /**
     * Parse product IDs từ JSON string
     *
     * @param jsonString JSON string
     * @return danh sách product IDs
     */
    private List<Integer> parseProductIds(final String jsonString) {
        if (jsonString == null || jsonString.trim().isEmpty()) {
            return List.of();
        }
        return Arrays.stream(jsonString.split(",")).map(String::trim).filter(s -> !s.isEmpty())
                .map(Integer::parseInt).collect(Collectors.toList());
    }

    /**
     * Parse category names từ JSON string
     *
     * @param jsonString JSON string
     * @return danh sách category names
     */
    private List<String> parseCategoryNames(final String jsonString) {
        if (jsonString == null || jsonString.trim().isEmpty()) {
            return List.of();
        }
        return Arrays.stream(jsonString.split(",")).map(String::trim).filter(s -> !s.isEmpty())
                .collect(Collectors.toList());
    }
}

