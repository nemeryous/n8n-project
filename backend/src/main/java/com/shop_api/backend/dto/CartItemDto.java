package com.shop_api.backend.dto;

import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.shop_api.backend.entity.CartItem;

import lombok.Data;

@Data
public class CartItemDto {

  @JsonProperty("id")
  private Integer id;

  @JsonProperty("product_id")
  private Integer productId;

  @JsonProperty("product_name")
  private String productName;

  @JsonProperty("customer_id")
  private Integer customerId;

  @JsonProperty("quantity")
  private Integer quantity;

  @JsonProperty("cart_id")
  private Integer cartId;

  @JsonProperty("unit_price")
  private Double unitPrice;

  @JsonProperty("total_price")
  private Double totalPrice;

  public static CartItemDto fromEntity(CartItem cartItem) {
    CartItemDto dto = new CartItemDto();

    dto.setId(cartItem.getId());
    dto.setProductId(cartItem.getProductId());
    dto.setCustomerId(cartItem.getCustomerId());
    dto.setQuantity(cartItem.getQuantity());
    dto.setCartId(cartItem.getCartId());
    dto.setUnitPrice(cartItem.getUnitPrice());
    dto.setTotalPrice(cartItem.getTotalPrice());

    return dto;
  }

  public static List<CartItemDto> fromEntities(List<CartItem> cartItems, Map<Integer, String> productsMap) {
    return cartItems.stream().map(cartItem -> {
      CartItemDto dto = fromEntity(cartItem);
      dto.setProductName(productsMap.get(cartItem.getProductId()));
      return dto;
    }).toList();
  }
}