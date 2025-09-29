package com.shop_api.backend.service.product;

import com.shop_api.backend.common.PageResponse;
import com.shop_api.backend.dto.CreateProductDto;
import com.shop_api.backend.dto.ProductDto;
import com.shop_api.backend.dto.UpdateProductDto;

public interface ProductService {

  PageResponse<ProductDto> getAllProducts(int page, int size, String sortBy, String sortDir, String search);

  ProductDto getProductById(Integer id);

  ProductDto createProduct(CreateProductDto dto);

  ProductDto updateProduct(Integer id, UpdateProductDto dto);

  boolean deleteProduct(Integer id);

}