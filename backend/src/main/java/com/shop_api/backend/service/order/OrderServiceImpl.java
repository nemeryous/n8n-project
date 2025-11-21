package com.shop_api.backend.service.order;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;
import com.shop_api.backend.constant.CartStatus;
import com.shop_api.backend.constant.OrderStatus;
import com.shop_api.backend.dto.OrderDto;
import com.shop_api.backend.entity.Cart;
import com.shop_api.backend.entity.CartItem;
import com.shop_api.backend.entity.Order;
import com.shop_api.backend.entity.OrderItem;
import com.shop_api.backend.entity.Product;
import com.shop_api.backend.exception.BadRequestException;
import com.shop_api.backend.exception.ResourceNotFoundException;
import com.shop_api.backend.repository.CartItemRepository;
import com.shop_api.backend.repository.CartRepository;
import com.shop_api.backend.repository.OrderItemRepository;
import com.shop_api.backend.repository.OrderRepository;
import com.shop_api.backend.repository.ProductRepository;
import com.shop_api.backend.service.coupon.CouponService;
import com.shop_api.backend.service.n8n.N8nWebHookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class OrderServiceImpl implements OrderService {
    @Autowired
    private N8nWebHookService n8nWebHookService;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderItemRepository orderItemRepository;

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private CartItemRepository cartItemRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CouponService couponService;

    @Override
    public Order createOrderFromCart(Integer customerId, Integer cartId, String shippingAddress,
            String phoneNumber, String notes, String couponCode) {
        // Find active cart
        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new ResourceNotFoundException("Giỏ hàng", "id", cartId));

        if (!cart.getCustomerId().equals(customerId)) {
            throw new BadRequestException("Giỏ hàng không thuộc về khách hàng này");
        }

        if (cart.getStatus() != CartStatus.ACTIVE) {
            throw new BadRequestException("Giỏ hàng không ở trạng thái hoạt động");
        }

        // Get cart items
        final List<CartItem> cartItems = cartItemRepository.findByCartId(cartId);
        if (cartItems.isEmpty()) {
            throw new BadRequestException("Giỏ hàng trống");
        }

        // Calculate total amount before discount
        BigDecimal totalAmount = calculateTotalAmount(cartItems);

        // Get product IDs and categories for coupon validation
        final List<Integer> productIds =
                cartItems.stream().map(CartItem::getProductId).collect(Collectors.toList());

        final List<String> categories = cartItems.stream().map(cartItem -> {
            final Product product =
                    productRepository.findById(cartItem.getProductId()).orElse(null);
            return product != null ? product.getCategory() : null;
        }).filter(category -> category != null).distinct().collect(Collectors.toList());

        // Validate and apply coupon if provided
        Integer appliedCouponId = null;
        if (couponCode != null && !couponCode.trim().isEmpty()) {
            final var validation = couponService.validateCoupon(couponCode, customerId, totalAmount,
                    productIds, categories);

            if (validation.getIsValid()) {
                // Apply discount
                totalAmount = validation.getFinalAmount();
                appliedCouponId = couponService.getCouponByCode(couponCode).getId();
            } else {
                throw new BadRequestException(validation.getMessage());
            }
        }

        // Create order
        final Order order = new Order();
        order.setCustomerId(customerId);
        order.setCartId(cartId);
        order.setOrderDate(Instant.now());
        order.setStatus(OrderStatus.PENDING);
        order.setTotalAmount(totalAmount);
        order.setShippingAddress(shippingAddress);
        order.setPhoneNumber(phoneNumber);
        order.setNotes(notes);
        order.setCreatedAt(Instant.now());
        order.setUpdatedAt(Instant.now());

        final Order savedOrder = orderRepository.save(order);

        // Create order items
        createOrderItems(savedOrder.getId(), cartItems);

        // Apply coupon (increment usage count) after order is created successfully
        if (appliedCouponId != null) {
            couponService.applyCoupon(appliedCouponId);
        }

        // Update cart status to COMPLETED
        cart.setStatus(CartStatus.COMPLETED);
        cart.setUpdatedAt(Instant.now());
        cartRepository.save(cart);

        n8nWebHookService.triggerOrderCompletedWebhook(OrderDto.fromEntity(savedOrder));
        return savedOrder;
    }

    @Override
    public Order getOrderById(Integer orderId) {
        return orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Đơn hàng", "id", orderId));
    }

    @Override
    public List<OrderItem> getOrderItems(Integer orderId) {
        return orderItemRepository.findByOrderId(orderId);
    }

    @Override
    public List<Order> getCustomerOrders(Integer customerId) {
        return orderRepository.findByCustomerId(customerId);
    }

    @Override
    public Order updateOrderStatus(Integer orderId, OrderStatus status) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        order.setStatus(status);
        order.setUpdatedAt(Instant.now());

        if (status.equals(OrderStatus.DELIVERED)) {
            // n8nWebHookService.triggerOrderDeliveredWebhook(orderId);
        } else {
            n8nWebHookService.triggerShippingUpdatedWebhook(orderId);
        }
        return orderRepository.save(order);
    }

    @Override
    public List<Order> getOrdersByStatus(OrderStatus status) {
        return orderRepository.findByStatus(status);
    }

    // Private helper methods
    private BigDecimal calculateTotalAmount(List<CartItem> cartItems) {
        BigDecimal total = BigDecimal.ZERO;

        for (CartItem cartItem : cartItems) {
            Product product = productRepository.findById(cartItem.getProductId()).orElseThrow(
                    () -> new ResourceNotFoundException("Sản phẩm", "id", cartItem.getProductId()));

            BigDecimal unitPrice = BigDecimal.valueOf(product.getPrice());
            BigDecimal itemTotal = unitPrice.multiply(BigDecimal.valueOf(cartItem.getQuantity()));
            total = total.add(itemTotal);
        }

        return total;
    }

    private void createOrderItems(Integer orderId, List<CartItem> cartItems) {
        List<OrderItem> orderItems = cartItems.stream().map(cartItem -> {
            Product product = productRepository.findById(cartItem.getProductId()).orElseThrow(
                    () -> new ResourceNotFoundException("Sản phẩm", "id", cartItem.getProductId()));

            OrderItem orderItem = new OrderItem();
            orderItem.setOrderId(orderId);
            orderItem.setProductId(cartItem.getProductId());
            orderItem.setQuantity(cartItem.getQuantity());
            orderItem.setUnitPrice(BigDecimal.valueOf(product.getPrice()));
            orderItem.setTotalPrice(BigDecimal.valueOf(product.getPrice())
                    .multiply(BigDecimal.valueOf(cartItem.getQuantity())));
            orderItem.setProductName(product.getName());

            return orderItem;
        }).collect(Collectors.toList());

        orderItemRepository.saveAll(orderItems);
    }
}
