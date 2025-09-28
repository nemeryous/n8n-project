package com.shop_api.backend.service.product;

import java.util.List;

import com.shop_api.backend.dto.CreateProductDto;
import com.shop_api.backend.dto.ProductDto;
import com.shop_api.backend.dto.UpdateProductDto;

public interface ProductService {
  
  List<ProductDto> getAllProducts();
  
  ProductDto getProductById(Integer id);
  
  ProductDto createProduct(CreateProductDto dto);
  
  ProductDto updateProduct(Integer id, UpdateProductDto dto);
  
  boolean deleteProduct(Integer id);
  
  List<ProductDto> searchProductsByName(String name);
  
  List<ProductDto> getProductsByCategory(String category);
  

}