package com.shop_api.backend.controller;

import java.util.List;
import com.shop_api.backend.dto.CustomerDto;
import com.shop_api.backend.dto.request.UpdateRoleRequest;
import com.shop_api.backend.dto.response.ApiResponse;
import com.shop_api.backend.entity.Customer;
import com.shop_api.backend.exception.ResourceNotFoundException;
import com.shop_api.backend.repository.CustomerRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Admin controller Chỉ dành cho users có role ADMIN
 */
@Slf4j
@RestController
@RequestMapping("${api.prefix}/admin")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
@io.swagger.v3.oas.annotations.tags.Tag(name = "Admin",
        description = "API quản trị - Chỉ dành cho ADMIN. Yêu cầu JWT token với role ADMIN.")
public class AdminController {

    private final CustomerRepository customerRepository;

    /**
     * Lấy danh sách tất cả customers Chỉ ADMIN mới có quyền truy cập
     */
    @io.swagger.v3.oas.annotations.Operation(summary = "Lấy danh sách tất cả khách hàng",
            description = "Chỉ ADMIN mới có quyền truy cập. Yêu cầu JWT token với role ADMIN.")
    @io.swagger.v3.oas.annotations.responses.ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200",
                    description = "Lấy danh sách thành công"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403",
                    description = "Không có quyền truy cập (chỉ ADMIN)")})
    @io.swagger.v3.oas.annotations.security.SecurityRequirement(name = "bearerAuth")
    @GetMapping("/customers")
    public ResponseEntity<ApiResponse<List<CustomerDto>>> getAllCustomers() {
        log.info("GET /admin/customers - Admin accessing all customers");

        List<Customer> customers = customerRepository.findAll();
        List<CustomerDto> customerDtos = customers.stream().map(CustomerDto::fromEntity).toList();

        return ResponseEntity.ok(ApiResponse.success(customerDtos));
    }

    /**
     * Lấy thông tin customer theo ID Chỉ ADMIN mới có quyền truy cập
     */
    @io.swagger.v3.oas.annotations.Operation(summary = "Lấy thông tin khách hàng theo ID",
            description = "Chỉ ADMIN mới có quyền truy cập")
    @io.swagger.v3.oas.annotations.security.SecurityRequirement(name = "bearerAuth")
    @GetMapping("/customers/{id}")
    public ResponseEntity<ApiResponse<CustomerDto>> getCustomerById(
            @io.swagger.v3.oas.annotations.Parameter(description = "ID của khách hàng",
                    required = true) @PathVariable Integer id) {
        log.info("GET /admin/customers/{} - Admin accessing customer", id);

        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Khách hàng", "id", id));

        return ResponseEntity.ok(ApiResponse.success(CustomerDto.fromEntity(customer)));
    }

    /**
     * Cập nhật role của customer Chỉ ADMIN mới có quyền thực hiện
     */
    @io.swagger.v3.oas.annotations.Operation(summary = "Cập nhật role của khách hàng",
            description = "Chỉ ADMIN mới có quyền thực hiện. Có thể thay đổi role giữa USER và ADMIN.")
    @io.swagger.v3.oas.annotations.security.SecurityRequirement(name = "bearerAuth")
    @PutMapping("/customers/{id}/role")
    public ResponseEntity<ApiResponse<CustomerDto>> updateCustomerRole(
            @io.swagger.v3.oas.annotations.Parameter(description = "ID của khách hàng",
                    required = true) @PathVariable Integer id,
            @Valid @RequestBody UpdateRoleRequest request) {
        log.info("PUT /admin/customers/{}/role - Admin updating customer role to {}", id,
                request.getRole());

        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Khách hàng", "id", id));

        customer.setRole(request.getRole());
        Customer updatedCustomer = customerRepository.save(customer);

        log.info("Customer role updated successfully. Customer ID: {}, New Role: {}",
                updatedCustomer.getId(), updatedCustomer.getRole());

        return ResponseEntity.ok(ApiResponse.success("Cập nhật role thành công",
                CustomerDto.fromEntity(updatedCustomer)));
    }

    /**
     * Xóa customer Chỉ ADMIN mới có quyền thực hiện
     */
    @io.swagger.v3.oas.annotations.Operation(summary = "Xóa khách hàng",
            description = "Chỉ ADMIN mới có quyền thực hiện. Xóa vĩnh viễn khách hàng khỏi hệ thống.")
    @io.swagger.v3.oas.annotations.security.SecurityRequirement(name = "bearerAuth")
    @DeleteMapping("/customers/{id}")
    public ResponseEntity<ApiResponse<String>> deleteCustomer(
            @io.swagger.v3.oas.annotations.Parameter(description = "ID của khách hàng",
                    required = true) @PathVariable Integer id) {
        log.info("DELETE /admin/customers/{} - Admin deleting customer", id);

        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Khách hàng", "id", id));

        customerRepository.delete(customer);

        log.info("Customer deleted successfully. Customer ID: {}", id);
        return ResponseEntity.ok(ApiResponse.success("Xóa khách hàng thành công"));
    }

    /**
     * Health check endpoint cho admin service
     */
    @GetMapping("/health")
    public ResponseEntity<ApiResponse<String>> health() {
        return ResponseEntity.ok(ApiResponse.success("Dịch vụ quản trị đang hoạt động"));
    }
}

