package com.shop_api.backend.controller;

import com.shop_api.backend.dto.CartItemDto;
import com.shop_api.backend.dto.CartItemRequestDto;
import com.shop_api.backend.service.cartitem.CartItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("${api.prefix}/cart-items")
public class CartItemController {

  @Autowired
  private CartItemService cartItemService;

  @PostMapping
  public ResponseEntity<CartItemDto> createCartItem(@RequestBody CartItemRequestDto cartItemRequestDto) {
    CartItemDto createdCartItem = cartItemService.createCartItem(cartItemRequestDto);
    return new ResponseEntity<>(createdCartItem, HttpStatus.CREATED);
  }

  @GetMapping("/{id}")
  public ResponseEntity<CartItemDto> getCartItemById(@PathVariable Integer id) {
    CartItemDto cartItem = cartItemService.getCartItemById(id);
    return ResponseEntity.ok(cartItem);
  }

  @GetMapping
  public ResponseEntity<List<CartItemDto>> getAllCartItems() {
    List<CartItemDto> cartItems = cartItemService.getAllCartItems();
    return ResponseEntity.ok(cartItems);
  }

  @GetMapping("/customer/{customerId}")
  public ResponseEntity<List<CartItemDto>> getCartItemsByCustomerId(@PathVariable Integer customerId) {
    List<CartItemDto> cartItems = cartItemService.getCartItemsByCustomerId(customerId);
    return ResponseEntity.ok(cartItems);
  }

  @GetMapping("/product/{productId}")
  public ResponseEntity<List<CartItemDto>> getCartItemsByProductId(@PathVariable Integer productId) {
    List<CartItemDto> cartItems = cartItemService.getCartItemsByProductId(productId);
    return ResponseEntity.ok(cartItems);
  }

  @PutMapping("/{id}")
  public ResponseEntity<CartItemDto> updateCartItem(@PathVariable Integer id,
      @RequestBody CartItemRequestDto cartItemRequestDto) {
    CartItemDto updatedCartItem = cartItemService.updateCartItem(id, cartItemRequestDto);
    return ResponseEntity.ok(updatedCartItem);
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteCartItem(@PathVariable Integer id) {
    cartItemService.deleteCartItem(id);
    return ResponseEntity.noContent().build();
  }
}