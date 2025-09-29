package com.shop_api.backend.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.shop_api.backend.entity.Product;

import lombok.Data;

@Data
public class CreateProductDto {

  @JsonProperty("name")
  private String name;

  @JsonProperty("description")
  private String description;

  @JsonProperty("price")
  private Double price;

  @JsonProperty("stock_quantity")
  private Integer stockQuantity;

  @JsonProperty("category")
  private String category;

  @JsonProperty("image_url")
  private String imageUrl;

  public static Product toEntity(CreateProductDto dto) {
    Product product = new Product();
    product.setName(dto.getName());
    product.setDescription(dto.getDescription());
    product.setPrice(dto.getPrice());
    product.setStockQuantity(dto.getStockQuantity());
    product.setCategory(dto.getCategory());
    product.setImageUrl(dto.getImageUrl());
    return product;
  }
}