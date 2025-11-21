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
import com.shop_api.backend.service.order.OrderService;
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
@RequestMapping("${api.prefix}/orders")
@PreAuthorize("isAuthenticated()")
public class OrderController {
    @Autowired
    private N8nWebHookService n8nWebHookService;

    @Autowired
    private OrderService orderService;

    @PostMapping
    public ResponseEntity<OrderDto> createOrder(@RequestBody CheckoutRequestDto request,
            @AuthenticationPrincipal UserPrincipal userPrincipal) {
        try {
            // Set customerId from JWT if not provided
            Integer customerId = request.getCustomerId();
            if (customerId == null) {
                customerId = userPrincipal.getId();
            } else if (!customerId.equals(userPrincipal.getId())
                    && !userPrincipal.getAuthorities().stream()
                            .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            }

            Order order = orderService.createOrderFromCart(customerId, request.getCartId(),
                    request.getShippingAddress(), request.getPhoneNumber(), request.getNotes());

            n8nWebHookService.triggerOrderCompletedWebhook(OrderDto.fromEntity(order));
            return ResponseEntity.status(HttpStatus.CREATED).body(OrderDto.fromEntity(order));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<OrderDto> getOrder(@PathVariable Integer orderId,
            @AuthenticationPrincipal UserPrincipal userPrincipal) {
        try {
            Order order = orderService.getOrderById(orderId);
            // Check if user owns the order or is admin
            if (order.getCustomerId() != null && !order.getCustomerId().equals(userPrincipal.getId())
                    && !userPrincipal.getAuthorities().stream()
                            .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            }
            return ResponseEntity.ok(OrderDto.fromEntity(order));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/{orderId}/items")
    public ResponseEntity<List<OrderItemDto>> getOrderItems(@PathVariable Integer orderId,
            @AuthenticationPrincipal UserPrincipal userPrincipal) {
        Order order = orderService.getOrderById(orderId);
        // Check if user owns the order or is admin
        if (order.getCustomerId() != null && !order.getCustomerId().equals(userPrincipal.getId())
                && !userPrincipal.getAuthorities().stream()
                        .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        List<OrderItem> orderItems = orderService.getOrderItems(orderId);
        return ResponseEntity.ok(OrderItemDto.fromEntities(orderItems));
    }

    @GetMapping("/customer/{customerId}")
    @PreAuthorize("hasRole('ADMIN') or (isAuthenticated() and #customerId == authentication.principal.id)")
    public ResponseEntity<List<OrderDto>> getCustomerOrders(@PathVariable Integer customerId) {
        List<Order> orders = orderService.getCustomerOrders(customerId);
        return ResponseEntity.ok(OrderDto.fromEntities(orders));
    }

    @GetMapping("/customer/me")
    public ResponseEntity<List<OrderDto>> getMyOrders(
            @AuthenticationPrincipal UserPrincipal userPrincipal) {
        Integer customerId = userPrincipal.getId();
        List<Order> orders = orderService.getCustomerOrders(customerId);
        return ResponseEntity.ok(OrderDto.fromEntities(orders));
    }

    @PutMapping("/{orderId}/status")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<OrderDto> updateOrderStatus(@PathVariable Integer orderId,
            @RequestBody OrderStatusUpdateDto statusUpdate) {
        try {
            Order updatedOrder = orderService.updateOrderStatus(orderId, statusUpdate.getStatus());
            return ResponseEntity.ok(OrderDto.fromEntity(updatedOrder));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<OrderDto>> getOrdersByStatus(@RequestParam OrderStatus status) {
        List<Order> orders = orderService.getOrdersByStatus(status);
        return ResponseEntity.ok(OrderDto.fromEntities(orders));
    }
}
