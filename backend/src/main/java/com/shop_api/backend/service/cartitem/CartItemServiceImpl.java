package com.shop_api.backend.service.cartitem;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shop_api.backend.dto.CartItemDto;
import com.shop_api.backend.dto.CartItemRequestDto;
import com.shop_api.backend.entity.CartItem;
import com.shop_api.backend.entity.Product;
import com.shop_api.backend.repository.CartItemRepository;
import com.shop_api.backend.repository.ProductRepository;

import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class CartItemServiceImpl implements CartItemService {

  @Autowired
  private CartItemRepository cartItemRepository;

  @Autowired
  private ProductRepository productRepository;

  @Override
  public CartItemDto createCartItem(CartItemRequestDto cartItemRequestDto) {
    CartItem cartItem = new CartItem();
    cartItem.setProductId(cartItemRequestDto.getProductId());
    cartItem.setCustomerId(cartItemRequestDto.getCustomerId());
    cartItem.setQuantity(cartItemRequestDto.getQuantity());
    cartItem.setCartId(cartItemRequestDto.getCartId());

    Product product = productRepository.findById(cartItemRequestDto.getProductId())
        .orElseThrow(() -> new RuntimeException("Product not found with id: " + cartItemRequestDto.getProductId()));

    log.info("Need to check quantity in stock");

    cartItem.setUnitPrice(product.getPrice());
    cartItem.setTotalPrice(product.getPrice() * cartItemRequestDto.getQuantity());

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

    return CartItemDto.fromEntities(cartItems, getProductNamesMap(cartItems));
  }

  @Override
  public List<CartItemDto> getCartItemsByCustomerId(Integer customerId) {
    List<CartItem> cartItems = cartItemRepository.findByCustomerId(customerId);
    return CartItemDto.fromEntities(cartItems, getProductNamesMap(cartItems));
  }

  @Override
  public List<CartItemDto> getCartItemsByProductId(Integer productId) {
    List<CartItem> cartItems = cartItemRepository.findByProductId(productId);
    return CartItemDto.fromEntities(cartItems, getProductNamesMap(cartItems));
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

  private Map<Integer, String> getProductNamesMap(List<CartItem> cartItems) {
    List<Integer> productIds = cartItems.stream()
        .map(CartItem::getProductId)
        .distinct()
        .toList();

    List<Product> products = productRepository.findAllById(productIds);

    return products.stream().collect(
        java.util.stream.Collectors.toMap(Product::getId, Product::getName));
  }

}