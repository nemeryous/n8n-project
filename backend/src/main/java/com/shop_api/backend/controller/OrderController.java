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
import com.shop_api.backend.service.order.OrderService;

/**
 * Controller for order operations
 */
@RestController
@RequestMapping("${api.prefix}/orders")
public class OrderController {

  @Autowired
  private OrderService orderService;

  /**
   * Create order from cart
   */
  @PostMapping
  public ResponseEntity<OrderDto> createOrder(@RequestBody CheckoutRequestDto request) {
    try {
      Order order = orderService.createOrderFromCart(
          request.getCustomerId(),
          request.getCartId(),
          request.getShippingAddress(),
          request.getPhoneNumber(),
          request.getNotes());

      return ResponseEntity.status(HttpStatus.CREATED).body(OrderDto.fromEntity(order));
    } catch (RuntimeException e) {
      return ResponseEntity.badRequest().build();
    }
  }

  /**
   * Get order by ID
   */
  @GetMapping("/{orderId}")
  public ResponseEntity<OrderDto> getOrder(@PathVariable Integer orderId) {
    try {
      Order order = orderService.getOrderById(orderId);
      return ResponseEntity.ok(OrderDto.fromEntity(order));
    } catch (RuntimeException e) {
      return ResponseEntity.notFound().build();
    }
  }

  /**
   * Get order items for a specific order
   */
  @GetMapping("/{orderId}/items")
  public ResponseEntity<List<OrderItemDto>> getOrderItems(@PathVariable Integer orderId) {
    List<OrderItem> orderItems = orderService.getOrderItems(orderId);
    return ResponseEntity.ok(OrderItemDto.fromEntities(orderItems));
  }

  /**
   * Get orders by customer ID
   */
  @GetMapping("/customer/{customerId}")
  public ResponseEntity<List<OrderDto>> getCustomerOrders(@PathVariable Integer customerId) {
    List<Order> orders = orderService.getCustomerOrders(customerId);
    return ResponseEntity.ok(OrderDto.fromEntities(orders));
  }

  /**
   * Update order status
   */
  @PutMapping("/{orderId}/status")
  public ResponseEntity<OrderDto> updateOrderStatus(
      @PathVariable Integer orderId,
      @RequestBody OrderStatusUpdateDto statusUpdate) {
    try {
      Order updatedOrder = orderService.updateOrderStatus(orderId, statusUpdate.getStatus());
      return ResponseEntity.ok(OrderDto.fromEntity(updatedOrder));
    } catch (RuntimeException e) {
      return ResponseEntity.notFound().build();
    }
  }

  /**
   * Get orders by status
   */
  @GetMapping
  public ResponseEntity<List<OrderDto>> getOrdersByStatus(@RequestParam OrderStatus status) {
    List<Order> orders = orderService.getOrdersByStatus(status);
    return ResponseEntity.ok(OrderDto.fromEntities(orders));
  }
}