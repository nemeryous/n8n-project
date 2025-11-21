package com.shop_api.backend.controller;

import java.util.List;
import com.shop_api.backend.constant.OrderStatus;
import com.shop_api.backend.dto.CheckoutRequestDto;
import com.shop_api.backend.dto.OrderDto;
import com.shop_api.backend.dto.OrderItemDto;
import com.shop_api.backend.dto.OrderStatusUpdateDto;
import com.shop_api.backend.entity.Order;
import com.shop_api.backend.entity.OrderItem;
import com.shop_api.backend.security.UserPrincipal;
import com.shop_api.backend.service.n8n.N8nWebHookService;
import com.shop_api.backend.service.order.CheckoutService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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
@PreAuthorize("isAuthenticated()")
public class CheckoutController {
    @Autowired
    private N8nWebHookService n8nWebHookService;

    @Autowired
    private CheckoutService checkoutService;

    @PostMapping
    public ResponseEntity<OrderDto> checkout(@RequestBody CheckoutRequestDto request,
            @AuthenticationPrincipal UserPrincipal userPrincipal) {
        try {
            // Set customerId from JWT if not provided
            Integer customerId = request.getCustomerId();
            if (customerId == null) {
                customerId = userPrincipal.getId();
            } else if (!customerId.equals(userPrincipal.getId()) && !userPrincipal.getAuthorities()
                    .stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            }

            Order order = checkoutService.createOrderFromCart(customerId, request.getCartId(),
                    request.getShippingAddress(), request.getPhoneNumber(), request.getNotes(),
                    request.getCouponCode());

            return ResponseEntity.status(HttpStatus.CREATED).body(OrderDto.fromEntity(order));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/orders/{orderId}")
    public ResponseEntity<OrderDto> getOrder(@PathVariable Integer orderId,
            @AuthenticationPrincipal UserPrincipal userPrincipal) {
        try {
            Order order = checkoutService.getOrderById(orderId);
            // Check if user owns the order or is admin
            if (order.getCustomerId() != null
                    && !order.getCustomerId().equals(userPrincipal.getId())
                    && !userPrincipal.getAuthorities().stream()
                            .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            }
            return ResponseEntity.ok(OrderDto.fromEntity(order));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/orders/{orderId}/items")
    public ResponseEntity<List<OrderItemDto>> getOrderItems(@PathVariable Integer orderId,
            @AuthenticationPrincipal UserPrincipal userPrincipal) {
        Order order = checkoutService.getOrderById(orderId);
        // Check if user owns the order or is admin
        if (order.getCustomerId() != null && !order.getCustomerId().equals(userPrincipal.getId())
                && !userPrincipal.getAuthorities().stream()
                        .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        List<OrderItem> orderItems = checkoutService.getOrderItems(orderId);
        return ResponseEntity.ok(OrderItemDto.fromEntities(orderItems));
    }

    @GetMapping("/orders/customer/{customerId}")
    @PreAuthorize("hasRole('ADMIN') or (isAuthenticated() and #customerId == authentication.principal.id)")
    public ResponseEntity<List<OrderDto>> getCustomerOrders(@PathVariable Integer customerId) {
        List<Order> orders = checkoutService.getCustomerOrders(customerId);
        return ResponseEntity.ok(OrderDto.fromEntities(orders));
    }

    @GetMapping("/orders/customer/me")
    public ResponseEntity<List<OrderDto>> getMyOrders(
            @AuthenticationPrincipal UserPrincipal userPrincipal) {
        Integer customerId = userPrincipal.getId();
        List<Order> orders = checkoutService.getCustomerOrders(customerId);
        return ResponseEntity.ok(OrderDto.fromEntities(orders));
    }

    @PutMapping("/orders/{orderId}/status")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<OrderDto> updateOrderStatus(@PathVariable Integer orderId,
            @RequestBody OrderStatusUpdateDto statusUpdate) {
        try {
            Order updatedOrder =
                    checkoutService.updateOrderStatus(orderId, statusUpdate.getStatus());

            OrderDto orderDto = OrderDto.fromEntity(updatedOrder);
            if (orderDto.getStatus() == OrderStatus.PROCESSING) {
                n8nWebHookService.triggerShippingUpdatedWebhook(orderId);
            } else if (orderDto.getStatus() == OrderStatus.DELIVERED) {
                // n8nWebHookService.triggerOrderDeliveredWebhook(orderId);
            }

            return ResponseEntity.ok(OrderDto.fromEntity(updatedOrder));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/orders")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<OrderDto>> getOrdersByStatus(@RequestParam OrderStatus status) {
        List<Order> orders = checkoutService.getOrdersByStatus(status);
        return ResponseEntity.ok(OrderDto.fromEntities(orders));
    }

    @GetMapping("/orders/abandoned")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<OrderDto>> getAbandonedOrders() {
        List<Order> abandonedOrders = checkoutService.getAbandonedOrders();
        return ResponseEntity.ok(OrderDto.fromEntities(abandonedOrders));
    }

    @PostMapping("/orders/mark-abandoned")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<OrderDto>> markAbandonedOrders(
            @RequestParam(defaultValue = "24") int thresholdHours) {
        List<Order> abandonedOrders = checkoutService.markAbandonedOrders(thresholdHours);
        return ResponseEntity.ok(OrderDto.fromEntities(abandonedOrders));
    }

    @GetMapping("/analytics")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CheckoutService.OrderAnalytics> getOrderAnalytics() {
        CheckoutService.OrderAnalytics analytics = checkoutService.getOrderAnalytics();
        return ResponseEntity.ok(analytics);
    }
}
