package com.shop_api.backend.service.cartitem;

import com.shop_api.backend.dto.CartItemDto;
import com.shop_api.backend.dto.CartItemRequestDto;
import java.util.List;

public interface CartItemService {
  CartItemDto createCartItem(CartItemRequestDto cartItemRequestDto);

  CartItemDto getCartItemById(Integer id);

  List<CartItemDto> getAllCartItems();

  List<CartItemDto> getCartItemsByCustomerId(Integer customerId);

  List<CartItemDto> getCartItemsByProductId(Integer productId);

  CartItemDto updateCartItem(Integer id, CartItemRequestDto cartItemRequestDto);

  void deleteCartItem(Integer id);
}