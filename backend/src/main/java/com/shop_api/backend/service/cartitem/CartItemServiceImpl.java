package com.shop_api.backend.service.cartitem;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shop_api.backend.dto.CartItemDto;
import com.shop_api.backend.dto.CartItemRequestDto;
import com.shop_api.backend.entity.CartItem;
import com.shop_api.backend.repository.CartItemRepository;

import java.util.List;

@Service
public class CartItemServiceImpl implements CartItemService {

  @Autowired
  private CartItemRepository cartItemRepository;

  @Override
  public CartItemDto createCartItem(CartItemRequestDto cartItemRequestDto) {
    CartItem cartItem = new CartItem();
    cartItem.setProductId(cartItemRequestDto.getProductId());
    cartItem.setCustomerId(cartItemRequestDto.getCustomerId());
    cartItem.setQuantity(cartItemRequestDto.getQuantity());

    CartItem savedCartItem = cartItemRepository.save(cartItem);
    return CartItemDto.fromEntity(savedCartItem);
  }

  @Override
  public CartItemDto getCartItemById(Integer id) {
    CartItem cartItem = cartItemRepository.findById(id)
        .orElseThrow(() -> new RuntimeException("CartItem not found with id: " + id));
    return CartItemDto.fromEntity(cartItem);
  }

  @Override
  public List<CartItemDto> getAllCartItems() {
    List<CartItem> cartItems = cartItemRepository.findAll();
    return CartItemDto.fromEntities(cartItems);
  }

  @Override
  public List<CartItemDto> getCartItemsByCustomerId(Integer customerId) {
    List<CartItem> cartItems = cartItemRepository.findByCustomerId(customerId);
    return CartItemDto.fromEntities(cartItems);
  }

  @Override
  public List<CartItemDto> getCartItemsByProductId(Integer productId) {
    List<CartItem> cartItems = cartItemRepository.findByProductId(productId);
    return CartItemDto.fromEntities(cartItems);
  }

  @Override
  public CartItemDto updateCartItem(Integer id, CartItemRequestDto cartItemRequestDto) {
    CartItem existingCartItem = cartItemRepository.findById(id)
        .orElseThrow(() -> new RuntimeException("CartItem not found with id: " + id));

    existingCartItem.setProductId(cartItemRequestDto.getProductId());
    existingCartItem.setCustomerId(cartItemRequestDto.getCustomerId());
    existingCartItem.setQuantity(cartItemRequestDto.getQuantity());

    CartItem updatedCartItem = cartItemRepository.save(existingCartItem);
    return CartItemDto.fromEntity(updatedCartItem);
  }

  @Override
  public void deleteCartItem(Integer id) {
    CartItem cartItem = cartItemRepository.findById(id)
        .orElseThrow(() -> new RuntimeException("CartItem not found with id: " + id));
    cartItemRepository.delete(cartItem);
  }

}