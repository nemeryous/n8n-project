package com.shop_api.backend.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.shop_api.backend.constant.OrderStatus;
import com.shop_api.backend.dto.CheckoutRequestDto;
import com.shop_api.backend.dto.OrderDto;
import com.shop_api.backend.dto.OrderItemDto;
import com.shop_api.backend.dto.OrderStatusUpdateDto;
import com.shop_api.backend.entity.Order;
import com.shop_api.backend.entity.OrderItem;
import com.shop_api.backend.service.order.CheckoutService;


@RestController
@RequestMapping("${api.prefix}/checkout")
public class CheckoutController {

  @Autowired
  private CheckoutService checkoutService;

  /**
   * Main checkout endpoint - creates order from cart
   */
  @PostMapping
  public ResponseEntity<OrderDto> checkout(@RequestBody CheckoutRequestDto request) {
    try {
      Order order = checkoutService.createOrderFromCart(
          request.getCustomerId(),
          request.getCartId(),
          request.getShippingAddress(),
          request.getPhoneNumber(),
          request.getNotes()
      );
      
      return ResponseEntity.status(HttpStatus.CREATED).body(OrderDto.fromEntity(order));
    } catch (RuntimeException e) {
      return ResponseEntity.badRequest().build();
    }
  }

  /**
   * Get order by ID
   */
  @GetMapping("/orders/{orderId}")
  public ResponseEntity<OrderDto> getOrder(@PathVariable Integer orderId) {
    try {
      Order order = checkoutService.getOrderById(orderId);
      return ResponseEntity.ok(OrderDto.fromEntity(order));
    } catch (RuntimeException e) {
      return ResponseEntity.notFound().build();
    }
  }

  /**
   * Get order items for a specific order
   */
  @GetMapping("/orders/{orderId}/items")
  public ResponseEntity<List<OrderItemDto>> getOrderItems(@PathVariable Integer orderId) {
    List<OrderItem> orderItems = checkoutService.getOrderItems(orderId);
    return ResponseEntity.ok(OrderItemDto.fromEntities(orderItems));
  }

  /**
   * Get orders by customer ID
   */
  @GetMapping("/orders/customer/{customerId}")
  public ResponseEntity<List<OrderDto>> getCustomerOrders(@PathVariable Integer customerId) {
    List<Order> orders = checkoutService.getCustomerOrders(customerId);
    return ResponseEntity.ok(OrderDto.fromEntities(orders));
  }

  /**
   * Update order status - for admin and n8n workflows
   */
  @PutMapping("/orders/{orderId}/status")
  public ResponseEntity<OrderDto> updateOrderStatus(
      @PathVariable Integer orderId,
      @RequestBody OrderStatusUpdateDto statusUpdate) {
    try {
      Order updatedOrder = checkoutService.updateOrderStatus(orderId, statusUpdate.getStatus());
      return ResponseEntity.ok(OrderDto.fromEntity(updatedOrder));
    } catch (RuntimeException e) {
      return ResponseEntity.notFound().build();
    }
  }

  /**
   * Get orders by status - useful for n8n workflow queries
   */
  @GetMapping("/orders")
  public ResponseEntity<List<OrderDto>> getOrdersByStatus(@RequestParam OrderStatus status) {
    List<Order> orders = checkoutService.getOrdersByStatus(status);
    return ResponseEntity.ok(OrderDto.fromEntities(orders));
  }

  /**
   * Get abandoned orders - specifically for n8n webhook
   */
  @GetMapping("/orders/abandoned")
  public ResponseEntity<List<OrderDto>> getAbandonedOrders() {
    List<Order> abandonedOrders = checkoutService.getAbandonedOrders();
    return ResponseEntity.ok(OrderDto.fromEntities(abandonedOrders));
  }

  /**
   * Mark abandoned orders - scheduled endpoint for n8n automation
   */
  @PostMapping("/orders/mark-abandoned")
  public ResponseEntity<List<OrderDto>> markAbandonedOrders(
      @RequestParam(defaultValue = "24") int thresholdHours) {
    List<Order> abandonedOrders = checkoutService.markAbandonedOrders(thresholdHours);
    return ResponseEntity.ok(OrderDto.fromEntities(abandonedOrders));
  }

  /**
   * Get order analytics - for n8n dashboards
   */
  @GetMapping("/analytics")
  public ResponseEntity<CheckoutService.OrderAnalytics> getOrderAnalytics() {
    CheckoutService.OrderAnalytics analytics = checkoutService.getOrderAnalytics();
    return ResponseEntity.ok(analytics);
  }
}