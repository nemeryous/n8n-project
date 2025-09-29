package com.shop_api.backend.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.shop_api.backend.entity.CartItem;

import lombok.Data;

@Data
public class CartItemDto {

  @JsonProperty("id")
  private Integer id;

  @JsonProperty("product_id")
  private Integer productId;

  @JsonProperty("customer_id")
  private Integer customerId;

  @JsonProperty("quantity")
  private Integer quantity;

  public static CartItemDto fromEntity(CartItem cartItem) {
    CartItemDto dto = new CartItemDto();
    
    dto.setId(cartItem.getId());
    dto.setProductId(cartItem.getProductId());
    dto.setCustomerId(cartItem.getCustomerId());
    dto.setQuantity(cartItem.getQuantity());

    return dto;
  }

  public static List<CartItemDto> fromEntities(List<CartItem> cartItems) {
    return cartItems.stream().map(CartItemDto::fromEntity).toList();
  }
}