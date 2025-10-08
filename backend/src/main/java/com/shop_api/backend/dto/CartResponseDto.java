package com.shop_api.backend.dto;

import java.time.Instant;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.shop_api.backend.constant.CartStatus;
import com.shop_api.backend.entity.Cart;
import com.shop_api.backend.entity.CartItem;

import lombok.Data;

@Data
public class CartResponseDto {
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

  private List<CartItemDto> items;

@JsonProperty("total_prices")
  private Double totalPrice;

  public static CartResponseDto createCartResponseDto(Cart cart, List<CartItem> items, Double totalPrice,
      Map<Integer, String> productsMap) {

    CartResponseDto dto = new CartResponseDto();

    dto.setId(cart.getId());
    dto.setCustomerId(cart.getCustomerId());
    dto.setCreatedAt(cart.getCreatedAt());
    dto.setUpdatedAt(cart.getUpdatedAt());
    dto.setStatus(cart.getStatus());
    dto.setItems(CartItemDto.fromEntities(items, productsMap));
    dto.setTotalPrice(totalPrice);

    return dto;
  }
}
