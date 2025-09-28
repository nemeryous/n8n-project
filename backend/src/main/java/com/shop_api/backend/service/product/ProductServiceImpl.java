package com.shop_api.backend.service.product;

import java.util.List;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shop_api.backend.dto.CreateProductDto;
import com.shop_api.backend.dto.ProductDto;
import com.shop_api.backend.dto.UpdateProductDto;
import com.shop_api.backend.entity.Product;
import com.shop_api.backend.repository.ProductRepository;

@Service
public class ProductServiceImpl implements ProductService {

  @Autowired
  private ProductRepository productRepository;

  @Override
  public List<ProductDto> getAllProducts() {
    return ProductDto.fromEntities(productRepository.findAll());
  }

  @Override
  public ProductDto getProductById(Integer id) {
    return ProductDto.fromEntity(
        productRepository.findById(id).orElseThrow(() -> new RuntimeException("Product not found with id: " + id)));

  }

  @Override
  public ProductDto createProduct(CreateProductDto dto) {
    Product product = CreateProductDto.toEntity(dto);
    Product savedProduct = productRepository.save(product);
    return ProductDto.fromEntity(savedProduct);
  }

  @Override
  public ProductDto updateProduct(Integer id, UpdateProductDto dto) {
    Product product = productRepository.findById(id)
        .orElseThrow(() -> new RuntimeException("Product not found with id: " + id));

    product.setName(dto.getName());
    product.setDescription(dto.getDescription());
    product.setPrice(dto.getPrice());
    product.setStockQuantity(dto.getStockQuantity());
    product.setCategory(dto.getCategory());
    product.setImageUrl(dto.getImageUrl());
    Product updatedProduct = productRepository.save(product);

    return ProductDto.fromEntity(updatedProduct);
  }

  @Override
  public boolean deleteProduct(Integer id) {
    if (productRepository.existsById(id)) {
      productRepository.deleteById(id);

      return true;
    }

    return false;
  }

  @Override
  public List<ProductDto> searchProductsByName(String name) {
    return ProductDto.fromEntities(productRepository.findByNameContainingIgnoreCase(name));
  }

  @Override
  public List<ProductDto> getProductsByCategory(String category) {
    return ProductDto.fromEntities(productRepository.findByCategoryIgnoreCase(category));
  }

}