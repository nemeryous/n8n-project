package com.shop_api.backend.service;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.shop_api.backend.constant.CartStatus;
import com.shop_api.backend.constant.OrderStatus;
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

/**
 * Checkout service for n8n workflow automation
 * Handles order creation, abandonment tracking, and status updates
 */
@Service
@Transactional
public class CheckoutService {

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

  /**
   * Create order from cart - main checkout process
   */
  public Order createOrderFromCart(Integer customerId, Integer cartId, String shippingAddress, String phoneNumber, String notes) {
    // Find active cart
    Cart cart = cartRepository.findById(cartId)
        .orElseThrow(() -> new RuntimeException("Cart not found"));

    if (!cart.getCustomerId().equals(customerId)) {
      throw new RuntimeException("Cart does not belong to customer");
    }

    if (cart.getStatus() != CartStatus.ACTIVE) {
      throw new RuntimeException("Cart is not active");
    }

    // Get cart items by cart_id (updated relationship)
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

    return savedOrder;
  }

  /**
   * Calculate total amount from cart items
   */
  private BigDecimal calculateTotalAmount(List<CartItem> cartItems) {
    BigDecimal total = BigDecimal.ZERO;
    
    for (CartItem cartItem : cartItems) {
      Product product = productRepository.findById(cartItem.getProductId())
          .orElseThrow(() -> new RuntimeException("Product not found: " + cartItem.getProductId()));
      
      BigDecimal unitPrice = BigDecimal.valueOf(product.getPrice());
      BigDecimal itemTotal = unitPrice.multiply(BigDecimal.valueOf(cartItem.getQuantity()));
      total = total.add(itemTotal);
    }
    
    return total;
  }

  /**
   * Create order items from cart items
   */
  private void createOrderItems(Integer orderId, List<CartItem> cartItems) {
    List<OrderItem> orderItems = cartItems.stream().map(cartItem -> {
      Product product = productRepository.findById(cartItem.getProductId())
          .orElseThrow(() -> new RuntimeException("Product not found: " + cartItem.getProductId()));

      OrderItem orderItem = new OrderItem();
      orderItem.setOrderId(orderId);
      orderItem.setProductId(cartItem.getProductId());
      orderItem.setQuantity(cartItem.getQuantity());
      orderItem.setUnitPrice(BigDecimal.valueOf(product.getPrice()));
      orderItem.setTotalPrice(BigDecimal.valueOf(product.getPrice()).multiply(BigDecimal.valueOf(cartItem.getQuantity())));
      orderItem.setProductName(product.getName()); // Snapshot for n8n workflows
      
      return orderItem;
    }).collect(Collectors.toList());

    orderItemRepository.saveAll(orderItems);
  }

  /**
   * Mark orders as abandoned for n8n workflow processing
   * Should be called by scheduled task
   */
  public List<Order> markAbandonedOrders(int abandonmentThresholdHours) {
    Instant cutoffTime = Instant.now().minus(abandonmentThresholdHours, ChronoUnit.HOURS);
    
    List<Order> pendingOrders = orderRepository.findPendingOrdersOlderThan(OrderStatus.PENDING, cutoffTime);
    
    List<Order> abandonedOrders = pendingOrders.stream().map(order -> {
      order.setStatus(OrderStatus.ABANDONED);
      order.setAbandonedAt(Instant.now());
      order.setUpdatedAt(Instant.now());
      return order;
    }).collect(Collectors.toList());

    return orderRepository.saveAll(abandonedOrders);
  }

  /**
   * Update order status - for n8n workflow integration
   */
  public Order updateOrderStatus(Integer orderId, OrderStatus newStatus) {
    Order order = orderRepository.findById(orderId)
        .orElseThrow(() -> new RuntimeException("Order not found"));

    order.setStatus(newStatus);
    order.setUpdatedAt(Instant.now());

    // Set abandoned timestamp if marking as abandoned
    if (newStatus == OrderStatus.ABANDONED && order.getAbandonedAt() == null) {
      order.setAbandonedAt(Instant.now());
    }

    return orderRepository.save(order);
  }

  /**
   * Get orders by status - for n8n workflow queries
   */
  public List<Order> getOrdersByStatus(OrderStatus status) {
    return orderRepository.findByStatus(status);
  }

  /**
   * Get abandoned orders - specifically for n8n processing
   */
  public List<Order> getAbandonedOrders() {
    return orderRepository.findByStatusAndAbandonedAtIsNotNull(OrderStatus.ABANDONED);
  }

  /**
   * Get order with items by order ID
   */
  public Order getOrderById(Integer orderId) {
    return orderRepository.findById(orderId)
        .orElseThrow(() -> new RuntimeException("Order not found"));
  }

  /**
   * Get order items for a specific order
   */
  public List<OrderItem> getOrderItems(Integer orderId) {
    return orderItemRepository.findByOrderId(orderId);
  }

  /**
   * Get customer orders
   */
  public List<Order> getCustomerOrders(Integer customerId) {
    return orderRepository.findByCustomerId(customerId);
  }

  /**
   * Get order analytics for n8n dashboards
   */
  public OrderAnalytics getOrderAnalytics() {
    OrderAnalytics analytics = new OrderAnalytics();
    analytics.setPendingCount(orderRepository.countByStatus(OrderStatus.PENDING));
    analytics.setAbandonedCount(orderRepository.countByStatus(OrderStatus.ABANDONED));
    analytics.setCompletedCount(orderRepository.countByStatus(OrderStatus.DELIVERED));
    analytics.setRecentOrders(orderRepository.findRecentOrders());
    
    return analytics;
  }

  /**
   * Inner class for order analytics
   */
  public static class OrderAnalytics {
    private long pendingCount;
    private long abandonedCount;
    private long completedCount;
    private List<Order> recentOrders;

    // Getters and setters
    public long getPendingCount() { return pendingCount; }
    public void setPendingCount(long pendingCount) { this.pendingCount = pendingCount; }
    
    public long getAbandonedCount() { return abandonedCount; }
    public void setAbandonedCount(long abandonedCount) { this.abandonedCount = abandonedCount; }
    
    public long getCompletedCount() { return completedCount; }
    public void setCompletedCount(long completedCount) { this.completedCount = completedCount; }
    
    public List<Order> getRecentOrders() { return recentOrders; }
    public void setRecentOrders(List<Order> recentOrders) { this.recentOrders = recentOrders; }
  }
}