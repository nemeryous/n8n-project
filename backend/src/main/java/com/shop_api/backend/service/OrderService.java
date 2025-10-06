package com.shop_api.backend.service;

import java.util.List;

import com.shop_api.backend.constant.OrderStatus;
import com.shop_api.backend.entity.Order;
import com.shop_api.backend.entity.OrderItem;

public interface OrderService {
    
    /**
     * Create order from cart
     */
    Order createOrderFromCart(Integer customerId, Integer cartId, String shippingAddress, String phoneNumber, String notes);
    
    /**
     * Get order by ID
     */
    Order getOrderById(Integer orderId);
    
    /**
     * Get order items for a specific order
     */
    List<OrderItem> getOrderItems(Integer orderId);
    
    /**
     * Get orders by customer ID
     */
    List<Order> getCustomerOrders(Integer customerId);
    
    /**
     * Update order status
     */
    Order updateOrderStatus(Integer orderId, OrderStatus status);
    
    /**
     * Get orders by status
     */
    List<Order> getOrdersByStatus(OrderStatus status);
}