package com.shop_api.backend.dto;

import java.time.Instant;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.shop_api.backend.constant.CartStatus;
import com.shop_api.backend.entity.Cart;

import lombok.Data;

@Data
public class CartDto {

  @JsonProperty("id")
  private Integer id;

  @JsonProperty("customer_id")
  private Integer customerId;

  @JsonProperty("created_at")
  private Instant createdAt;

  @JsonProperty("updated_at")
  private Instant updatedAt;

  @JsonProperty("status")
  private CartStatus status;

  public static CartDto fromEntity(Cart cart) {
    CartDto dto = new CartDto();

    dto.setId(cart.getId());
    dto.setCustomerId(cart.getCustomerId());
    dto.setCreatedAt(cart.getCreatedAt());
    dto.setUpdatedAt(cart.getUpdatedAt());
    dto.setStatus(cart.getStatus());

    return dto;
  }

  public static List<CartDto> fromEntities(List<Cart> carts) {
    return carts.stream().map(CartDto::fromEntity).toList();
  }
}