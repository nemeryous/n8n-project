package com.shop_api.backend.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.shop_api.backend.dto.CartDto;
import com.shop_api.backend.dto.CartRequestDto;
import com.shop_api.backend.service.cart.CartService;

@RestController
@RequestMapping("${api.prefix}/carts")
public class CartController {

  @Autowired
  private CartService cartService;

  @GetMapping
  public ResponseEntity<List<CartDto>> getAllCarts() {
    List<CartDto> carts = cartService.getAllCarts();
    return ResponseEntity.ok(carts);
  }

  @GetMapping("/{id}")
  public ResponseEntity<CartDto> getCartById(@PathVariable Integer id) {
    CartDto cart = cartService.getCartById(id);
    return ResponseEntity.ok(cart);
  }

  @PostMapping
  public ResponseEntity<CartDto> createCart(@RequestBody CartRequestDto dto) {
    CartDto createdCart = cartService.createCart(dto);
    return ResponseEntity.status(HttpStatus.CREATED).body(createdCart);
  }

  @PutMapping("/{id}")
  public ResponseEntity<CartDto> updateCart(@PathVariable Integer id,
      @RequestBody CartRequestDto dto) {
    CartDto updatedCart = cartService.updateCart(id, dto);
    return ResponseEntity.ok(updatedCart);
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteCart(@PathVariable Integer id) {
    boolean deleted = cartService.deleteCart(id);
    return deleted ? ResponseEntity.noContent().build()
        : ResponseEntity.notFound().build();
  }

  @GetMapping("/customer/{customerId}")
  public ResponseEntity<List<CartDto>> getCartsByCustomerId(@PathVariable Integer customerId) {
    List<CartDto> carts = cartService.getCartsByCustomerId(customerId);
    return ResponseEntity.ok(carts);
  }

  @GetMapping("/customer/{customerId}/active")
  public ResponseEntity<CartDto> getActiveCartByCustomerId(@PathVariable Integer customerId) {
    CartDto activeCart = cartService.getActiveCartByCustomerId(customerId);
    return ResponseEntity.ok(activeCart);
  }

  /**
   * Get or create cart by session ID (for guest users)
   */
  @GetMapping("/session/{sessionId}")
  public ResponseEntity<CartDto> getCartBySession(@PathVariable String sessionId) {
    CartDto cart = cartService.getOrCreateCartBySession(sessionId);
    return ResponseEntity.ok(cart);
  }

  /**
   * Get or create cart by customer ID (for logged-in users)
   */
  @GetMapping("/customer/{customerId}/current")
  public ResponseEntity<CartDto> getCurrentCart(@PathVariable Integer customerId) {
    CartDto cart = cartService.getOrCreateCartByCustomer(customerId);
    return ResponseEntity.ok(cart);
  }
}