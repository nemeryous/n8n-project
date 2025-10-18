package com.shop_api.backend.service.order;

import java.util.List;
import com.shop_api.backend.constant.OrderStatus;
import com.shop_api.backend.entity.Order;
import com.shop_api.backend.entity.OrderItem;

public interface OrderService {
    Order createOrderFromCart(Integer customerId, Integer cartId, String shippingAddress,
            String phoneNumber, String notes);

    Order getOrderById(Integer orderId);

    List<OrderItem> getOrderItems(Integer orderId);

    List<Order> getCustomerOrders(Integer customerId);

    Order updateOrderStatus(Integer orderId, OrderStatus status);

    List<Order> getOrdersByStatus(OrderStatus status);
}
