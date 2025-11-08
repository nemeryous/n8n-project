package com.shop_api.backend.controller;

import java.util.List;
import com.shop_api.backend.constant.OrderStatus;
import com.shop_api.backend.dto.CheckoutRequestDto;
import com.shop_api.backend.dto.OrderDto;
import com.shop_api.backend.dto.OrderItemDto;
import com.shop_api.backend.dto.OrderStatusUpdateDto;
import com.shop_api.backend.entity.Order;
import com.shop_api.backend.entity.OrderItem;
import com.shop_api.backend.service.n8n.N8nWebHookService;
import com.shop_api.backend.service.order.CheckoutService;
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

@RestController
@RequestMapping("${api.prefix}/checkout")
public class CheckoutController {
    @Autowired
    private N8nWebHookService n8nWebHookService;

    @Autowired
    private CheckoutService checkoutService;

    @PostMapping
    public ResponseEntity<OrderDto> checkout(@RequestBody CheckoutRequestDto request) {
        try {
            Order order = checkoutService.createOrderFromCart(request.getCustomerId(),
                    request.getCartId(), request.getShippingAddress(), request.getPhoneNumber(),
                    request.getNotes());

            return ResponseEntity.status(HttpStatus.CREATED).body(OrderDto.fromEntity(order));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/orders/{orderId}")
    public ResponseEntity<OrderDto> getOrder(@PathVariable Integer orderId) {
        try {
            Order order = checkoutService.getOrderById(orderId);
            return ResponseEntity.ok(OrderDto.fromEntity(order));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/orders/{orderId}/items")
    public ResponseEntity<List<OrderItemDto>> getOrderItems(@PathVariable Integer orderId) {
        List<OrderItem> orderItems = checkoutService.getOrderItems(orderId);
        return ResponseEntity.ok(OrderItemDto.fromEntities(orderItems));
    }

    @GetMapping("/orders/customer/{customerId}")
    public ResponseEntity<List<OrderDto>> getCustomerOrders(@PathVariable Integer customerId) {
        List<Order> orders = checkoutService.getCustomerOrders(customerId);
        return ResponseEntity.ok(OrderDto.fromEntities(orders));
    }

    @PutMapping("/orders/{orderId}/status")
    public ResponseEntity<OrderDto> updateOrderStatus(@PathVariable Integer orderId,
            @RequestBody OrderStatusUpdateDto statusUpdate) {
        try {
            Order updatedOrder = checkoutService.updateOrderStatus(orderId, statusUpdate.getStatus());

            OrderDto orderDto = OrderDto.fromEntity(updatedOrder);
            if (orderDto.getStatus() == OrderStatus.PROCESSING) {
                n8nWebHookService.triggerShippingUpdatedWebhook(orderId);
            } else if (orderDto.getStatus() == OrderStatus.DELIVERED) {

                n8nWebHookService.triggerOrderDeliveredWebhook(orderId);
            }

            return ResponseEntity.ok(OrderDto.fromEntity(updatedOrder));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/orders")
    public ResponseEntity<List<OrderDto>> getOrdersByStatus(@RequestParam OrderStatus status) {
        List<Order> orders = checkoutService.getOrdersByStatus(status);
        return ResponseEntity.ok(OrderDto.fromEntities(orders));
    }

    @GetMapping("/orders/abandoned")
    public ResponseEntity<List<OrderDto>> getAbandonedOrders() {
        List<Order> abandonedOrders = checkoutService.getAbandonedOrders();
        return ResponseEntity.ok(OrderDto.fromEntities(abandonedOrders));
    }

    @PostMapping("/orders/mark-abandoned")
    public ResponseEntity<List<OrderDto>> markAbandonedOrders(
            @RequestParam(defaultValue = "24") int thresholdHours) {
        List<Order> abandonedOrders = checkoutService.markAbandonedOrders(thresholdHours);
        return ResponseEntity.ok(OrderDto.fromEntities(abandonedOrders));
    }

    @GetMapping("/analytics")
    public ResponseEntity<CheckoutService.OrderAnalytics> getOrderAnalytics() {
        CheckoutService.OrderAnalytics analytics = checkoutService.getOrderAnalytics();
        return ResponseEntity.ok(analytics);
    }
}
