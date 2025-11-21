package com.shop_api.backend.service.cartitem;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import com.shop_api.backend.constant.CartStatus;
import com.shop_api.backend.dto.CartItemDto;
import com.shop_api.backend.dto.CartItemRequestDto;
import com.shop_api.backend.entity.Cart;
import com.shop_api.backend.entity.CartItem;
import com.shop_api.backend.entity.Product;
import com.shop_api.backend.exception.BadRequestException;
import com.shop_api.backend.exception.ResourceNotFoundException;
import com.shop_api.backend.repository.CartItemRepository;
import com.shop_api.backend.repository.CartRepository;
import com.shop_api.backend.repository.ProductRepository;
import com.shop_api.backend.service.cart.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class CartItemServiceImpl implements CartItemService {

    @Autowired
    private CartItemRepository cartItemRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private CartService cartService;

    @Override
    @Transactional
    public CartItemDto createCartItem(final CartItemRequestDto cartItemRequestDto) {
        // Validate required fields
        if (cartItemRequestDto.getProductId() == null) {
            throw new BadRequestException("Product ID không được để trống");
        }
        if (cartItemRequestDto.getCustomerId() == null) {
            throw new BadRequestException("Customer ID không được để trống");
        }
        if (cartItemRequestDto.getQuantity() == null || cartItemRequestDto.getQuantity() <= 0) {
            throw new BadRequestException("Số lượng phải lớn hơn 0");
        }

        // Get or create active cart for customer if cartId is not provided
        final Integer cartId;
        if (cartItemRequestDto.getCartId() == null) {
            log.debug("Cart ID not provided, getting or creating active cart for customer: {}",
                    cartItemRequestDto.getCustomerId());
            final var cartDto =
                    cartService.getOrCreateCartByCustomer(cartItemRequestDto.getCustomerId());
            cartId = cartDto.getId();
        } else {
            cartId = cartItemRequestDto.getCartId();
            // Validate cart exists and belongs to customer
            final Cart cart = cartRepository.findById(cartId)
                    .orElseThrow(() -> new ResourceNotFoundException("Giỏ hàng", "id", cartId));

            if (!cart.getCustomerId().equals(cartItemRequestDto.getCustomerId())) {
                throw new BadRequestException("Giỏ hàng không thuộc về khách hàng này");
            }

            if (cart.getStatus() != CartStatus.ACTIVE) {
                throw new BadRequestException("Giỏ hàng không ở trạng thái hoạt động");
            }
        }

        // Check if product already exists in cart
        final Optional<CartItem> existingCartItemOpt = cartItemRepository
                .findByCartIdAndProductId(cartId, cartItemRequestDto.getProductId());

        if (existingCartItemOpt.isPresent()) {
            // Update quantity if product already exists in cart
            final CartItem existingCartItem = existingCartItemOpt.get();
            final int newQuantity =
                    existingCartItem.getQuantity() + cartItemRequestDto.getQuantity();
            existingCartItem.setQuantity(newQuantity);
            existingCartItem.setTotalPrice(existingCartItem.getUnitPrice() * newQuantity);
            final CartItem updatedCartItem = cartItemRepository.save(existingCartItem);
            log.info("Updated existing cart item. Cart ID: {}, Product ID: {}, New Quantity: {}",
                    cartId, cartItemRequestDto.getProductId(), newQuantity);
            return CartItemDto.fromEntity(updatedCartItem);
        }

        // Get product to validate and calculate price
        final Product product = productRepository.findById(cartItemRequestDto.getProductId())
                .orElseThrow(() -> new ResourceNotFoundException("Sản phẩm", "id",
                        cartItemRequestDto.getProductId()));

        // Check stock quantity
        if (product.getStockQuantity() < cartItemRequestDto.getQuantity()) {
            throw new BadRequestException(
                    String.format("Số lượng sản phẩm không đủ. Hiện có: %d, yêu cầu: %d",
                            product.getStockQuantity(), cartItemRequestDto.getQuantity()));
        }

        // Create new cart item
        final CartItem cartItem = new CartItem();
        cartItem.setCartId(cartId);
        cartItem.setProductId(cartItemRequestDto.getProductId());
        cartItem.setCustomerId(cartItemRequestDto.getCustomerId());
        cartItem.setQuantity(cartItemRequestDto.getQuantity());
        cartItem.setUnitPrice(product.getPrice());
        cartItem.setTotalPrice(product.getPrice() * cartItemRequestDto.getQuantity());

        final CartItem savedCartItem = cartItemRepository.save(cartItem);
        log.info("Created new cart item. Cart ID: {}, Product ID: {}, Quantity: {}", cartId,
                cartItemRequestDto.getProductId(), cartItemRequestDto.getQuantity());

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
        Optional<Cart> activeCartOpt =
                cartRepository.findByCustomerIdAndStatus(customerId, CartStatus.ACTIVE);

        if (activeCartOpt.isEmpty()) {
            return Collections.emptyList();
        }

        Integer activeCartId = activeCartOpt.get().getId();
        List<CartItem> cartItems = cartItemRepository.findByCartId(activeCartId);

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
        List<Integer> productIds =
                cartItems.stream().map(CartItem::getProductId).distinct().toList();

        List<Product> products = productRepository.findAllById(productIds);

        return products.stream()
                .collect(java.util.stream.Collectors.toMap(Product::getId, Product::getName));
    }

}
