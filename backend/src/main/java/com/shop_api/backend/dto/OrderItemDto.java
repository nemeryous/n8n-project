package com.shop_api.backend.dto;

import java.math.BigDecimal;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.shop_api.backend.entity.OrderItem;

import lombok.Data;

/**
 * DTO for OrderItem response
 */
@Data
public class OrderItemDto {

  @JsonProperty("id")
  private Integer id;

  @JsonProperty("order_id")
  private Integer orderId;

  @JsonProperty("product_id")
  private Integer productId;

  @JsonProperty("quantity")
  private Integer quantity;

  @JsonProperty("unit_price")
  private BigDecimal unitPrice;

  @JsonProperty("total_price")
  private BigDecimal totalPrice;

  @JsonProperty("product_name")
  private String productName;

  public static OrderItemDto fromEntity(OrderItem orderItem) {
    OrderItemDto dto = new OrderItemDto();
    dto.setId(orderItem.getId());
    dto.setOrderId(orderItem.getOrderId());
    dto.setProductId(orderItem.getProductId());
    dto.setQuantity(orderItem.getQuantity());
    dto.setUnitPrice(orderItem.getUnitPrice());
    dto.setTotalPrice(orderItem.getTotalPrice());
    dto.setProductName(orderItem.getProductName());
    return dto;
  }

  public static List<OrderItemDto> fromEntities(List<OrderItem> orderItems) {
    return orderItems.stream().map(OrderItemDto::fromEntity).toList();
  }
}