package com.shop_api.backend.controller;

import com.shop_api.backend.dto.CartItemDto;
import com.shop_api.backend.dto.CartItemRequestDto;
import com.shop_api.backend.security.UserPrincipal;
import com.shop_api.backend.service.cartitem.CartItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("${api.prefix}/cart-items")
@PreAuthorize("isAuthenticated()")
public class CartItemController {

  @Autowired
  private CartItemService cartItemService;

  @PostMapping
  public ResponseEntity<CartItemDto> createCartItem(@RequestBody CartItemRequestDto cartItemRequestDto,
      @AuthenticationPrincipal UserPrincipal userPrincipal) {
    // Set customerId from JWT if not provided
    if (cartItemRequestDto.getCustomerId() == null) {
      cartItemRequestDto.setCustomerId(userPrincipal.getId());
    } else if (!cartItemRequestDto.getCustomerId().equals(userPrincipal.getId())
        && !userPrincipal.getAuthorities().stream()
            .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))) {
      return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }
    CartItemDto createdCartItem = cartItemService.createCartItem(cartItemRequestDto);
    return new ResponseEntity<>(createdCartItem, HttpStatus.CREATED);
  }

  @GetMapping("/{id}")
  public ResponseEntity<CartItemDto> getCartItemById(@PathVariable Integer id,
      @AuthenticationPrincipal UserPrincipal userPrincipal) {
    CartItemDto cartItem = cartItemService.getCartItemById(id);
    // Check if user owns the cart item or is admin
    if (cartItem.getCustomerId() != null && !cartItem.getCustomerId().equals(userPrincipal.getId())
        && !userPrincipal.getAuthorities().stream()
            .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))) {
      return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }
    return ResponseEntity.ok(cartItem);
  }

  @GetMapping
  @PreAuthorize("hasRole('ADMIN')")
  public ResponseEntity<List<CartItemDto>> getAllCartItems() {
    List<CartItemDto> cartItems = cartItemService.getAllCartItems();
    return ResponseEntity.ok(cartItems);
  }

  @GetMapping("/customer/{customerId}")
  @PreAuthorize("hasRole('ADMIN') or (isAuthenticated() and #customerId == authentication.principal.id)")
  public ResponseEntity<List<CartItemDto>> getCartItemsByCustomerId(@PathVariable Integer customerId) {
    List<CartItemDto> cartItems = cartItemService.getCartItemsByCustomerId(customerId);
    return ResponseEntity.ok(cartItems);
  }

  @GetMapping("/customer/me")
  public ResponseEntity<List<CartItemDto>> getMyCartItems(
      @AuthenticationPrincipal UserPrincipal userPrincipal) {
    Integer customerId = userPrincipal.getId();
    List<CartItemDto> cartItems = cartItemService.getCartItemsByCustomerId(customerId);
    return ResponseEntity.ok(cartItems);
  }

  @GetMapping("/product/{productId}")
  @PreAuthorize("hasRole('ADMIN')")
  public ResponseEntity<List<CartItemDto>> getCartItemsByProductId(@PathVariable Integer productId) {
    List<CartItemDto> cartItems = cartItemService.getCartItemsByProductId(productId);
    return ResponseEntity.ok(cartItems);
  }

  @PutMapping("/{id}")
  public ResponseEntity<CartItemDto> updateCartItem(@PathVariable Integer id,
      @RequestBody CartItemRequestDto cartItemRequestDto,
      @AuthenticationPrincipal UserPrincipal userPrincipal) {
    CartItemDto existingCartItem = cartItemService.getCartItemById(id);
    // Check if user owns the cart item or is admin
    if (existingCartItem.getCustomerId() != null
        && !existingCartItem.getCustomerId().equals(userPrincipal.getId())
        && !userPrincipal.getAuthorities().stream()
            .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))) {
      return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }
    // Set customerId from JWT if not provided
    if (cartItemRequestDto.getCustomerId() == null) {
      cartItemRequestDto.setCustomerId(userPrincipal.getId());
    }
    CartItemDto updatedCartItem = cartItemService.updateCartItem(id, cartItemRequestDto);
    return ResponseEntity.ok(updatedCartItem);
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteCartItem(@PathVariable Integer id,
      @AuthenticationPrincipal UserPrincipal userPrincipal) {
    CartItemDto existingCartItem = cartItemService.getCartItemById(id);
    // Check if user owns the cart item or is admin
    if (existingCartItem.getCustomerId() != null
        && !existingCartItem.getCustomerId().equals(userPrincipal.getId())
        && !userPrincipal.getAuthorities().stream()
            .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))) {
      return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }
    cartItemService.deleteCartItem(id);
    return ResponseEntity.noContent().build();
  }
}