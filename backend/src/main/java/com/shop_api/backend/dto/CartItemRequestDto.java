package com.shop_api.backend.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class CartItemRequestDto {

  @JsonProperty("product_id")
  private Integer productId;

  @JsonProperty("customer_id")
  private Integer customerId;

  @JsonProperty("quantity")
  private Integer quantity;

  @JsonProperty("cart_id")
  private Integer cartId;
}