package com.shop_api.backend.service.cart;

import java.util.List;

import com.shop_api.backend.dto.CartDto;
import com.shop_api.backend.dto.CartRequestDto;
import com.shop_api.backend.dto.CartResponseDto;

public interface CartService {
  
  List<CartDto> getAllCarts();
  
  CartResponseDto getCartById(Integer id);
  
  CartDto createCart(CartRequestDto dto);
  
  CartDto updateCart(Integer id, CartRequestDto dto);
  
  boolean deleteCart(Integer id);
  
  List<CartDto> getCartsByCustomerId(Integer customerId);
  
  CartDto getActiveCartByCustomerId(Integer customerId);
  
  CartDto getOrCreateCartBySession(String sessionId);
  
  CartDto getOrCreateCartByCustomer(Integer customerId);

  CartDto abandonCart(Integer id);
}