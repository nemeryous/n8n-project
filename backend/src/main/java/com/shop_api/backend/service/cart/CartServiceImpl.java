package com.shop_api.backend.service.cart;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shop_api.backend.constant.CartStatus;
import com.shop_api.backend.dto.CartDto;
import com.shop_api.backend.dto.CartRequestDto;
import com.shop_api.backend.entity.Cart;
import com.shop_api.backend.repository.CartRepository;

@Service
public class CartServiceImpl implements CartService {

  @Autowired
  private CartRepository cartRepository;

  @Override
  public List<CartDto> getAllCarts() {
    return CartDto.fromEntities(cartRepository.findAll());
  }

  @Override
  public CartDto getCartById(Integer id) {
    Cart cart = cartRepository.findById(id)
        .orElseThrow(() -> new RuntimeException("Cart not found with id: " + id));

    return CartDto.fromEntity(cart);
  }

  @Override
  public CartDto createCart(CartRequestDto dto) {
    Cart cart = new Cart();

    cart.setCustomerId(dto.getCustomerId());
    cart.setStatus(dto.getStatus() != null ? dto.getStatus() : CartStatus.ACTIVE);
    cart.setCreatedAt(Instant.now());
    cart.setUpdatedAt(Instant.now());

    Cart savedCart = cartRepository.save(cart);

    return CartDto.fromEntity(savedCart);
  }

  @Override
  public CartDto updateCart(Integer id, CartRequestDto dto) {
    Cart cart = cartRepository.findById(id)
        .orElseThrow(() -> new RuntimeException("Cart not found with id: " + id));

    if (dto.getCustomerId() != null) {
      cart.setCustomerId(dto.getCustomerId());
    }
    if (dto.getStatus() != null) {
      cart.setStatus(dto.getStatus());
    }

    cart.setUpdatedAt(Instant.now());

    Cart updatedCart = cartRepository.save(cart);

    return CartDto.fromEntity(updatedCart);
  }

  @Override
  public boolean deleteCart(Integer id) {
    if (cartRepository.existsById(id)) {
      cartRepository.deleteById(id);

      return true;
    }

    return false;
  }

  @Override
  public List<CartDto> getCartsByCustomerId(Integer customerId) {
    return CartDto.fromEntities(cartRepository.findByCustomerId(customerId));
  }

  @Override
  public CartDto getActiveCartByCustomerId(Integer customerId) {
    Cart cart = cartRepository.findByCustomerIdAndStatus(customerId, CartStatus.ACTIVE)
        .orElseThrow(() -> new RuntimeException("No active cart found for customer id: " + customerId));

    return CartDto.fromEntity(cart);
  }

}