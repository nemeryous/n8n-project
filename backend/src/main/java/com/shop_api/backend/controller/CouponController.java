package com.shop_api.backend.controller;

import java.math.BigDecimal;
import java.util.List;
import com.shop_api.backend.constant.CustomerSegment;
import com.shop_api.backend.dto.CouponDto;
import com.shop_api.backend.dto.CouponValidationDto;
import com.shop_api.backend.dto.CreateCouponDto;
import com.shop_api.backend.dto.UpdateCouponDto;
import com.shop_api.backend.dto.response.ApiResponse;
import com.shop_api.backend.service.coupon.CouponService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Controller cho Coupon management Tuân thủ RESTful API design principles
 */
@Slf4j
@RestController
@RequestMapping("${api.prefix}/coupons")
@RequiredArgsConstructor
@Tag(name = "Coupon", description = "API quản lý mã giảm giá")
public class CouponController {

    private final CouponService couponService;

    @Operation(summary = "Tạo coupon mới", description = "Chỉ ADMIN mới có quyền tạo coupon",
            security = @SecurityRequirement(name = "bearerAuth"))
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<CouponDto>> createCoupon(
            @Valid @RequestBody final CreateCouponDto createCouponDto) {
        log.info("POST /coupons - Creating new coupon with code: {}", createCouponDto.getCode());

        final CouponDto couponDto = couponService.createCoupon(createCouponDto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Tạo coupon thành công", couponDto));
    }

    @Operation(summary = "Lấy coupon theo ID",
            description = "Chỉ ADMIN mới có quyền xem chi tiết coupon")
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<CouponDto>> getCouponById(@Parameter(
            description = "ID của coupon", required = true) @PathVariable final Integer id) {
        log.debug("GET /coupons/{} - Fetching coupon", id);

        final CouponDto couponDto = couponService.getCouponById(id);
        return ResponseEntity.ok(ApiResponse.success(couponDto));
    }

    @Operation(summary = "Lấy coupon theo code",
            description = "Public endpoint để lấy thông tin coupon")
    @GetMapping("/code/{code}")
    @PreAuthorize("permitAll()")
    public ResponseEntity<ApiResponse<CouponDto>> getCouponByCode(@Parameter(
            description = "Mã coupon", required = true) @PathVariable final String code) {
        log.debug("GET /coupons/code/{} - Fetching coupon by code", code);

        final CouponDto couponDto = couponService.getCouponByCode(code);
        return ResponseEntity.ok(ApiResponse.success(couponDto));
    }

    @Operation(summary = "Lấy tất cả coupons",
            description = "Chỉ ADMIN mới có quyền xem danh sách tất cả coupons")
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<List<CouponDto>>> getAllCoupons() {
        log.debug("GET /coupons - Fetching all coupons");

        final List<CouponDto> coupons = couponService.getAllCoupons();
        return ResponseEntity.ok(ApiResponse.success(coupons));
    }

    @Operation(summary = "Validate coupon",
            description = "Public endpoint để validate coupon trước khi áp dụng",
            security = @SecurityRequirement(name = "bearerAuth"))
    @GetMapping("/validate")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponse<CouponValidationDto>> validateCoupon(
            @Parameter(description = "Mã coupon", required = true) @RequestParam final String code,
            @Parameter(description = "Giá trị đơn hàng",
                    required = true) @RequestParam final BigDecimal amount,
            @Parameter(description = "Danh sách ID sản phẩm (optional)") @RequestParam(
                    required = false) final List<Integer> productIds,
            @Parameter(description = "Danh sách category (optional)") @RequestParam(
                    required = false) final List<String> categories,
            @Parameter(
                    description = "ID khách hàng (optional, lấy từ JWT nếu không có)") @RequestParam(
                            required = false) final Integer customerId) {
        log.debug("GET /coupons/validate - Validating coupon: {} with amount: {}", code, amount);

        // TODO: Lấy customerId từ JWT nếu không có trong request
        final Integer finalCustomerId = customerId; // Tạm thời, sẽ cải thiện sau

        final CouponValidationDto validation =
                couponService.validateCoupon(code, finalCustomerId, amount, productIds, categories);
        return ResponseEntity.ok(ApiResponse.success(validation));
    }

    @Operation(summary = "Lấy danh sách coupon available",
            description = "Lấy danh sách coupon có thể sử dụng cho customer segment")
    @GetMapping("/available")
    @PreAuthorize("permitAll()")
    public ResponseEntity<ApiResponse<List<CouponDto>>> getAvailableCoupons(
            @Parameter(description = "Phân khúc khách hàng (optional)") @RequestParam(
                    required = false) final CustomerSegment segment) {
        log.debug("GET /coupons/available - Fetching available coupons for segment: {}", segment);

        final CustomerSegment finalSegment = segment != null ? segment : CustomerSegment.NEW;
        final List<CouponDto> coupons = couponService.getAvailableCouponsBySegment(finalSegment);
        return ResponseEntity.ok(ApiResponse.success(coupons));
    }

    @Operation(summary = "Cập nhật coupon", description = "Chỉ ADMIN mới có quyền cập nhật coupon",
            security = @SecurityRequirement(name = "bearerAuth"))
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<CouponDto>> updateCoupon(
            @Parameter(description = "ID của coupon",
                    required = true) @PathVariable final Integer id,
            @Valid @RequestBody final UpdateCouponDto updateCouponDto) {
        log.info("PUT /coupons/{} - Updating coupon", id);

        final CouponDto couponDto = couponService.updateCoupon(id, updateCouponDto);
        return ResponseEntity.ok(ApiResponse.success("Cập nhật coupon thành công", couponDto));
    }

    @Operation(summary = "Xóa coupon", description = "Chỉ ADMIN mới có quyền xóa coupon",
            security = @SecurityRequirement(name = "bearerAuth"))
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<String>> deleteCoupon(@Parameter(
            description = "ID của coupon", required = true) @PathVariable final Integer id) {
        log.info("DELETE /coupons/{} - Deleting coupon", id);

        couponService.deleteCoupon(id);
        return ResponseEntity.ok(ApiResponse.success("Xóa coupon thành công"));
    }
}

