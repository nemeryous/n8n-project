package com.shop_api.backend.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.shop_api.backend.entity.Product;

import lombok.Data;

@Data
public class ProductDto {

  @JsonProperty("id")
  private Integer id;

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

  public static ProductDto fromEntity(Product product) {

    return new ProductDto() {
      {
        setId(product.getId());
        setName(product.getName());
        setDescription(product.getDescription());
        setPrice(product.getPrice());
        setStockQuantity(product.getStockQuantity());
        setCategory(product.getCategory());
        setImageUrl(product.getImageUrl());
      }
    };
  }

  public static Product toEntity(ProductDto dto) {
    return new Product() {
      {
        setId(dto.getId());
        setName(dto.getName());
        setDescription(dto.getDescription());
        setPrice(dto.getPrice());
        setStockQuantity(dto.getStockQuantity());
        setCategory(dto.getCategory());
        setImageUrl(dto.getImageUrl());
      }
    };
  }

  public static List<ProductDto> fromEntities(List<Product> products) {
    return products.stream().map(ProductDto::fromEntity).toList();
  }
}