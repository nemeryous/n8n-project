package com.shop_api.backend.controller;

import com.shop_api.backend.dto.CustomerDto;
import com.shop_api.backend.dto.CustomerRequestDto;
import com.shop_api.backend.security.UserPrincipal;
import com.shop_api.backend.service.customer.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("${api.prefix}/customers")
public class CustomerController {

    @Autowired
    private CustomerService customerService;

    @PostMapping
    @PreAuthorize("permitAll()")
    public ResponseEntity<CustomerDto> createCustomer(@RequestBody CustomerRequestDto customerRequestDto) {
        CustomerDto createdCustomer = customerService.createCustomer(customerRequestDto);
        return new ResponseEntity<>(createdCustomer, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<CustomerDto> getCustomerById(@PathVariable Integer id,
            @AuthenticationPrincipal UserPrincipal userPrincipal) {
        CustomerDto customer = customerService.getCustomerById(id);
        // Check if user is viewing their own profile or is admin
        if (!id.equals(userPrincipal.getId())
                && !userPrincipal.getAuthorities().stream()
                        .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        return ResponseEntity.ok(customer);
    }

    @GetMapping("/me")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<CustomerDto> getMyProfile(
            @AuthenticationPrincipal UserPrincipal userPrincipal) {
        Integer customerId = userPrincipal.getId();
        CustomerDto customer = customerService.getCustomerById(customerId);
        return ResponseEntity.ok(customer);
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<CustomerDto>> getAllCustomers() {
        List<CustomerDto> customers = customerService.getAllCustomers();
        return ResponseEntity.ok(customers);
    }

    @GetMapping("/email/{email}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<CustomerDto> getCustomerByEmail(@PathVariable String email,
            @AuthenticationPrincipal UserPrincipal userPrincipal) {
        CustomerDto customer = customerService.getCustomerByEmail(email);
        // Check if user is viewing their own profile or is admin
        if (!email.equals(userPrincipal.getEmail())
                && !userPrincipal.getAuthorities().stream()
                        .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        return ResponseEntity.ok(customer);
    }

    @PutMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<CustomerDto> updateCustomer(@PathVariable Integer id,
            @RequestBody CustomerRequestDto customerRequestDto,
            @AuthenticationPrincipal UserPrincipal userPrincipal) {
        // Check if user is updating their own profile or is admin
        if (!id.equals(userPrincipal.getId())
                && !userPrincipal.getAuthorities().stream()
                        .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        CustomerDto updatedCustomer = customerService.updateCustomer(id, customerRequestDto);
        return ResponseEntity.ok(updatedCustomer);
    }

    @PutMapping("/me")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<CustomerDto> updateMyProfile(
            @RequestBody CustomerRequestDto customerRequestDto,
            @AuthenticationPrincipal UserPrincipal userPrincipal) {
        Integer customerId = userPrincipal.getId();
        CustomerDto updatedCustomer = customerService.updateCustomer(customerId, customerRequestDto);
        return ResponseEntity.ok(updatedCustomer);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or (isAuthenticated() and #id == authentication.principal.id)")
    public ResponseEntity<Void> deleteCustomer(@PathVariable Integer id) {
        customerService.deleteCustomer(id);
        return ResponseEntity.noContent().build();
    }
}