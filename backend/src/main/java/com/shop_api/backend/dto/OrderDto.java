package com.shop_api.backend.dto;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.shop_api.backend.constant.OrderStatus;
import com.shop_api.backend.entity.Order;

import lombok.Data;

/**
 * DTO for Order response
 */
@Data
public class OrderDto {

    @JsonProperty("id")
    private Integer id;

    @JsonProperty("customer_id")
    private Integer customerId;

    @JsonProperty("cart_id")
    private Integer cartId;

    @JsonProperty("order_date")
    private Instant orderDate;

    @JsonProperty("status")
    private OrderStatus status;

    @JsonProperty("total_amount")
    private BigDecimal totalAmount;

    @JsonProperty("shipping_address")
    private String shippingAddress;

    @JsonProperty("phone_number")
    private String phoneNumber;

    @JsonProperty("notes")
    private String notes;

    @JsonProperty("abandoned_at")
    private Instant abandonedAt;

    @JsonProperty("created_at")
    private Instant createdAt;

    @JsonProperty("updated_at")
    private Instant updatedAt;

    public static OrderDto fromEntity(Order order) {
        OrderDto dto = new OrderDto();
        dto.setId(order.getId());
        dto.setCustomerId(order.getCustomerId());
        dto.setCartId(order.getCartId());
        dto.setOrderDate(order.getOrderDate());
        dto.setStatus(order.getStatus());
        dto.setTotalAmount(order.getTotalAmount());
        dto.setShippingAddress(order.getShippingAddress());
        dto.setPhoneNumber(order.getPhoneNumber());
        dto.setNotes(order.getNotes());
        dto.setAbandonedAt(order.getAbandonedAt());
        dto.setCreatedAt(order.getCreatedAt());
        dto.setUpdatedAt(order.getUpdatedAt());
        return dto;
    }

    public static List<OrderDto> fromEntities(List<Order> orders) {
        return orders.stream().map(OrderDto::fromEntity).toList();
    }
}