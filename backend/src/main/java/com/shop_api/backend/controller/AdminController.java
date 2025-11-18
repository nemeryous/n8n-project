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
public class AdminController {

    private final CustomerRepository customerRepository;

    /**
     * Lấy danh sách tất cả customers Chỉ ADMIN mới có quyền truy cập
     */
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
    @GetMapping("/customers/{id}")
    public ResponseEntity<ApiResponse<CustomerDto>> getCustomerById(@PathVariable Integer id) {
        log.info("GET /admin/customers/{} - Admin accessing customer", id);

        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Khách hàng", "id", id));

        return ResponseEntity.ok(ApiResponse.success(CustomerDto.fromEntity(customer)));
    }

    /**
     * Cập nhật role của customer Chỉ ADMIN mới có quyền thực hiện
     */
    @PutMapping("/customers/{id}/role")
    public ResponseEntity<ApiResponse<CustomerDto>> updateCustomerRole(@PathVariable Integer id,
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
    @DeleteMapping("/customers/{id}")
    public ResponseEntity<ApiResponse<String>> deleteCustomer(@PathVariable Integer id) {
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

