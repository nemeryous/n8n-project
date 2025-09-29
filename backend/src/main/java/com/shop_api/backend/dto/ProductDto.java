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

    ProductDto dto = new ProductDto();
    dto.setId(product.getId());
    dto.setName(product.getName());
    dto.setDescription(product.getDescription());
    dto.setPrice(product.getPrice());
    dto.setStockQuantity(product.getStockQuantity());
    dto.setCategory(product.getCategory());
    dto.setImageUrl(product.getImageUrl());

    return dto;

  }

  public static Product toEntity(ProductDto dto) {
    Product product = new Product();
    product.setId(dto.getId());
    product.setName(dto.getName());
    product.setDescription(dto.getDescription());
    product.setPrice(dto.getPrice());
    product.setStockQuantity(dto.getStockQuantity());
    product.setCategory(dto.getCategory());
    product.setImageUrl(dto.getImageUrl());
    return product;
  }

  public static List<ProductDto> fromEntities(List<Product> products) {
    return products.stream().map(ProductDto::fromEntity).toList();
  }
}