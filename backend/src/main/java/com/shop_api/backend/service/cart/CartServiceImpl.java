package com.shop_api.backend.service.cart;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shop_api.backend.constant.CartStatus;
import com.shop_api.backend.dto.CartDto;
import com.shop_api.backend.dto.CartRequestDto;
import com.shop_api.backend.dto.CartResponseDto;
import com.shop_api.backend.entity.Cart;
import com.shop_api.backend.entity.CartItem;
import com.shop_api.backend.entity.CustomerBehavior;
import com.shop_api.backend.entity.Product;
import com.shop_api.backend.repository.CartItemRepository;
import com.shop_api.backend.repository.CartRepository;
import com.shop_api.backend.repository.CustomerBehaviorRepository;
import com.shop_api.backend.repository.ProductRepository;
import com.shop_api.backend.service.n8n.N8nWebHookService;

@Service
public class CartServiceImpl implements CartService {

  @Autowired
  private CartRepository cartRepository;

  @Autowired
  private N8nWebHookService n8nWebHookService;

  @Autowired
  private CartItemRepository cartItemRepository;

  @Autowired
  private ProductRepository productRepository;

  @Autowired
  private CustomerBehaviorRepository customerBehaviorRepository;

  @Override
  public List<CartDto> getAllCarts() {
    return CartDto.fromEntities(cartRepository.findAll());
  }

  @Override
  public CartResponseDto getCartById(Integer id) {
    Cart cart = cartRepository.findById(id)
        .orElseThrow(() -> new RuntimeException("Cart not found with id: " + id));

    List<CartItem> cartItems = cartItemRepository.findByCartId(cart.getId());

    List<Integer> productIds = cartItems.stream()
        .map(CartItem::getProductId)
        .collect(Collectors.toList());

    Map<Integer, String> productsMap = productRepository.findAllById(productIds).stream()
        .collect(Collectors.toMap(Product::getId, Product::getName));

    if (cartItems.isEmpty()) {
      CartResponseDto cartResponse = CartResponseDto.createCartResponseDto(cart, cartItems, 0.0, productsMap);

      return cartResponse;
    }

    Double totalPrice = cartItems.stream().reduce(0.0, (subtotal, item) -> {
      return subtotal + item.getTotalPrice();
    }, Double::sum);

    CartResponseDto cartResponse = CartResponseDto.createCartResponseDto(cart, cartItems, totalPrice, productsMap);

    return cartResponse;
  }

  @Override
  public CartDto createCart(CartRequestDto dto) {
    Cart cart = new Cart();

    cart.setCustomerId(dto.getCustomerId());
    cart.setStatus(dto.getStatus() != null ? dto.getStatus() : CartStatus.ACTIVE);
    cart.setCreatedAt(Instant.now());
    cart.setUpdatedAt(Instant.now());

    Cart savedCart = cartRepository.save(cart);

    n8nWebHookService.triggerCartCreatedWebhook(CartDto.fromEntity(savedCart));

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

  public CartDto abandonCart(Integer id) {
    Cart cart = cartRepository.findById(id)
        .orElseThrow(() -> new RuntimeException("Cart not found with id: " + id));
    cart.setStatus(CartStatus.ABANDONED);
    cart.setUpdatedAt(Instant.now());
    Cart updatedCart = cartRepository.save(cart);

    n8nWebHookService.triggerCartAbandonedWebhook(CartDto.fromEntity(updatedCart));

    CustomerBehavior customerBehavior = customerBehaviorRepository.findById(cart.getCustomerId())
        .orElse(null);

    if (customerBehavior != null) {
      customerBehavior.setAbandonmentCartCount(customerBehavior.getAbandonmentCartCount() + 1);
      customerBehaviorRepository.save(customerBehavior);
    }

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

  @Override
  public CartDto getOrCreateCartBySession(String sessionId) {
    // Try to find existing active cart by session
    Cart cart = cartRepository.findBySessionIdAndStatus(sessionId, CartStatus.ACTIVE)
        .orElse(null);

    if (cart == null) {
      // Create new cart for session
      cart = new Cart();
      cart.setSessionId(sessionId);
      cart.setStatus(CartStatus.ACTIVE);
      cart.setCreatedAt(Instant.now());
      cart.setUpdatedAt(Instant.now());
      cart = cartRepository.save(cart);
    }

    return CartDto.fromEntity(cart);
  }

  @Override
  public CartDto getOrCreateCartByCustomer(Integer customerId) {
    // Try to find existing active cart by customer
    Cart cart = cartRepository.findByCustomerIdAndStatus(customerId, CartStatus.ACTIVE)
        .orElse(null);

    if (cart == null) {
      // Create new cart for customer
      cart = new Cart();
      cart.setCustomerId(customerId);
      cart.setStatus(CartStatus.ACTIVE);
      cart.setCreatedAt(Instant.now());
      cart.setUpdatedAt(Instant.now());
      cart = cartRepository.save(cart);
    }

    return CartDto.fromEntity(cart);
  }

}