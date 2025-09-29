package com.shop_api.backend.service.product;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.shop_api.backend.common.PageResponse;
import com.shop_api.backend.dto.CreateProductDto;
import com.shop_api.backend.dto.ProductDto;
import com.shop_api.backend.dto.UpdateProductDto;
import com.shop_api.backend.entity.Product;
import com.shop_api.backend.repository.ProductRepository;
import com.shop_api.backend.specification.ProductSpecification;

@Service
public class ProductServiceImpl implements ProductService {

  @Autowired
  private ProductRepository productRepository;

  @Override
  public PageResponse<ProductDto> getAllProducts(int page, int size, String sortBy, String sortDir, String search) {
    Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.fromString(sortDir), sortBy));

    Specification<Product> spec = Specification.where(ProductSpecification.searchByKeyword(search));

    return new PageResponse<>(productRepository.findAll(spec, pageable).map(ProductDto::fromEntity));
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

}