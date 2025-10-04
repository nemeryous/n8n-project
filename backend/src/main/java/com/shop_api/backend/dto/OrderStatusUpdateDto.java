package com.shop_api.backend.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.shop_api.backend.constant.OrderStatus;

import lombok.Data;

/**
 * DTO for updating order status
 */
@Data
public class OrderStatusUpdateDto {

  @JsonProperty("status")
  private OrderStatus status;
}