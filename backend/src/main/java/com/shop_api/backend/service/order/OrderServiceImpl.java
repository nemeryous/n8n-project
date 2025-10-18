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
import com.shop_api.backend.repository.CartItemRepository;
import com.shop_api.backend.repository.CartRepository;
import com.shop_api.backend.repository.OrderItemRepository;
import com.shop_api.backend.repository.OrderRepository;
import com.shop_api.backend.repository.ProductRepository;
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

    @Override
    public Order createOrderFromCart(Integer customerId, Integer cartId, String shippingAddress,
            String phoneNumber, String notes) {
        // Find active cart
        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new RuntimeException("Cart not found"));

        if (!cart.getCustomerId().equals(customerId)) {
            throw new RuntimeException("Cart does not belong to customer");
        }

        if (cart.getStatus() != CartStatus.ACTIVE) {
            throw new RuntimeException("Cart is not active");
        }

        // Get cart items
        List<CartItem> cartItems = cartItemRepository.findByCartId(cartId);
        if (cartItems.isEmpty()) {
            throw new RuntimeException("Cart is empty");
        }

        // Calculate total amount
        BigDecimal totalAmount = calculateTotalAmount(cartItems);

        // Create order
        Order order = new Order();
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

        Order savedOrder = orderRepository.save(order);

        // Create order items
        createOrderItems(savedOrder.getId(), cartItems);

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
                .orElseThrow(() -> new RuntimeException("Order not found"));
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
            n8nWebHookService.triggerOrderDeliveredWebhook(orderId);
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
                    () -> new RuntimeException("Product not found: " + cartItem.getProductId()));

            BigDecimal unitPrice = BigDecimal.valueOf(product.getPrice());
            BigDecimal itemTotal = unitPrice.multiply(BigDecimal.valueOf(cartItem.getQuantity()));
            total = total.add(itemTotal);
        }

        return total;
    }

    private void createOrderItems(Integer orderId, List<CartItem> cartItems) {
        List<OrderItem> orderItems = cartItems.stream().map(cartItem -> {
            Product product = productRepository.findById(cartItem.getProductId()).orElseThrow(
                    () -> new RuntimeException("Product not found: " + cartItem.getProductId()));

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
