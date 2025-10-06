package com.shop_api.backend.service.cart;

import java.util.List;

import com.shop_api.backend.dto.CartDto;
import com.shop_api.backend.dto.CartRequestDto;

public interface CartService {
  
  List<CartDto> getAllCarts();
  
  CartDto getCartById(Integer id);
  
  CartDto createCart(CartRequestDto dto);
  
  CartDto updateCart(Integer id, CartRequestDto dto);
  
  boolean deleteCart(Integer id);
  
  List<CartDto> getCartsByCustomerId(Integer customerId);
  
  CartDto getActiveCartByCustomerId(Integer customerId);
  
  // Session-based cart methods
  CartDto getOrCreateCartBySession(String sessionId);
  
  CartDto getOrCreateCartByCustomer(Integer customerId);
}