package com.shop_api.backend.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.shop_api.backend.constant.CartStatus;

import lombok.Data;

@Data
public class CartRequestDto {

  @JsonProperty("customer_id")
  private Integer customerId;

  @JsonProperty("status")
  private CartStatus status;
}