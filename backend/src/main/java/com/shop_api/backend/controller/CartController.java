package com.shop_api.backend.controller;

import java.util.List;
import com.shop_api.backend.dto.CartDto;
import com.shop_api.backend.dto.CartRequestDto;
import com.shop_api.backend.dto.CartResponseDto;
import com.shop_api.backend.security.UserPrincipal;
import com.shop_api.backend.service.cart.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("${api.prefix}/carts")
@PreAuthorize("isAuthenticated()")
public class CartController {

    @Autowired
    private CartService cartService;

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<CartDto>> getAllCarts() {
        List<CartDto> carts = cartService.getAllCarts();
        return ResponseEntity.ok(carts);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CartResponseDto> getCartById(@PathVariable Integer id,
            @AuthenticationPrincipal UserPrincipal userPrincipal) {
        CartResponseDto cart = cartService.getCartById(id);
        // Check if user owns the cart or is admin
        if (!userPrincipal.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))
                && cart.getCustomerId() != null
                && !cart.getCustomerId().equals(userPrincipal.getId())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        return ResponseEntity.ok(cart);
    }

    @PostMapping
    public ResponseEntity<CartDto> createCart(@RequestBody CartRequestDto dto,
            @AuthenticationPrincipal UserPrincipal userPrincipal) {
        // Set customerId from JWT if not provided
        if (dto.getCustomerId() == null) {
            dto.setCustomerId(userPrincipal.getId());
        } else if (!dto.getCustomerId().equals(userPrincipal.getId())
                && !userPrincipal.getAuthorities().stream()
                        .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        CartDto createdCart = cartService.createCart(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdCart);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CartDto> updateCart(@PathVariable Integer id,
            @RequestBody CartRequestDto dto, @AuthenticationPrincipal UserPrincipal userPrincipal) {
        CartResponseDto existingCart = cartService.getCartById(id);
        // Check if user owns the cart or is admin
        if (existingCart.getCustomerId() != null
                && !existingCart.getCustomerId().equals(userPrincipal.getId())
                && !userPrincipal.getAuthorities().stream()
                        .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        CartDto updatedCart = cartService.updateCart(id, dto);
        return ResponseEntity.ok(updatedCart);
    }

    @PutMapping("/abandoned/{id}")
    public ResponseEntity<CartDto> abandonedCart(@PathVariable Integer id,
            @AuthenticationPrincipal UserPrincipal userPrincipal) {
        CartResponseDto existingCart = cartService.getCartById(id);
        // Check if user owns the cart or is admin
        if (existingCart.getCustomerId() != null
                && !existingCart.getCustomerId().equals(userPrincipal.getId())
                && !userPrincipal.getAuthorities().stream()
                        .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        CartDto updatedCart = cartService.abandonCart(id);
        return ResponseEntity.ok(updatedCart);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCart(@PathVariable Integer id,
            @AuthenticationPrincipal UserPrincipal userPrincipal) {
        CartResponseDto existingCart = cartService.getCartById(id);
        // Check if user owns the cart or is admin
        if (existingCart.getCustomerId() != null
                && !existingCart.getCustomerId().equals(userPrincipal.getId())
                && !userPrincipal.getAuthorities().stream()
                        .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        boolean deleted = cartService.deleteCart(id);
        return deleted ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }

    @GetMapping("/customer/{customerId}")
    @PreAuthorize("hasRole('ADMIN') or (isAuthenticated() and #customerId == authentication.principal.id)")
    public ResponseEntity<List<CartDto>> getCartsByCustomerId(@PathVariable Integer customerId,
            @AuthenticationPrincipal UserPrincipal userPrincipal) {
        List<CartDto> carts = cartService.getCartsByCustomerId(customerId);
        return ResponseEntity.ok(carts);
    }

    @GetMapping("/customer/{customerId}/active")
    @PreAuthorize("hasRole('ADMIN') or (isAuthenticated() and #customerId == authentication.principal.id)")
    public ResponseEntity<CartDto> getActiveCartByCustomerId(@PathVariable Integer customerId,
            @AuthenticationPrincipal UserPrincipal userPrincipal) {
        CartDto activeCart = cartService.getActiveCartByCustomerId(customerId);
        return ResponseEntity.ok(activeCart);
    }

    @GetMapping("/session/{sessionId}")
    public ResponseEntity<CartDto> getCartBySession(@PathVariable String sessionId) {
        CartDto cart = cartService.getOrCreateCartBySession(sessionId);
        return ResponseEntity.ok(cart);
    }

    @GetMapping("/customer/{customerId}/current")
    @PreAuthorize("hasRole('ADMIN') or (isAuthenticated() and #customerId == authentication.principal.id)")
    public ResponseEntity<CartDto> getCurrentCart(@PathVariable Integer customerId,
            @AuthenticationPrincipal UserPrincipal userPrincipal) {
        CartDto cart = cartService.getOrCreateCartByCustomer(customerId);
        return ResponseEntity.ok(cart);
    }

    @GetMapping("/current")
    public ResponseEntity<CartDto> getCurrentCartForAuthenticatedUser(
            @AuthenticationPrincipal UserPrincipal userPrincipal) {
        Integer customerId = userPrincipal.getId();
        CartDto cart = cartService.getOrCreateCartByCustomer(customerId);
        return ResponseEntity.ok(cart);
    }
}
