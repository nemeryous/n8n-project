package com.shop_api.backend.dto;

import com.shop_api.backend.constant.CartStatus;

import lombok.Data;

@Data
public class CartRequestDto {
  private Integer customerId;
  private CartStatus status;
}