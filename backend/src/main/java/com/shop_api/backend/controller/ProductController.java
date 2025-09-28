package com.shop_api.backend.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.shop_api.backend.common.PageResponse;
import com.shop_api.backend.dto.CreateProductDto;
import com.shop_api.backend.dto.ProductDto;
import com.shop_api.backend.dto.UpdateProductDto;
import com.shop_api.backend.service.product.ProductService;

@RestController
@RequestMapping("${api.prefix}/products")
public class ProductController {

  @Autowired
  private ProductService productService;

  @GetMapping
  public ResponseEntity<PageResponse<ProductDto>> getAllProducts(
    @RequestParam(defaultValue = "0") int page,
    @RequestParam(defaultValue = "10") int size,
    @RequestParam(defaultValue = "id") String sortBy, 
    @RequestParam(defaultValue = "asc") String sortDir,
    @RequestParam(required = false) String search
  ) {
    PageResponse<ProductDto> products = productService.getAllProducts(page, size, sortBy, sortDir, search);
    return ResponseEntity.ok(products);
  }

  @GetMapping("/{id}")
  public ResponseEntity<ProductDto> getProductById(@PathVariable Integer id) {
    ProductDto product = productService.getProductById(id);
    return ResponseEntity.ok(product);
  }

  @PostMapping
  public ResponseEntity<ProductDto> createProduct(@RequestBody CreateProductDto dto) {
    ProductDto createdProduct = productService.createProduct(dto);
    return ResponseEntity.status(HttpStatus.CREATED).body(createdProduct);
  }

  @PutMapping("/{id}")
  public ResponseEntity<ProductDto> updateProduct(@PathVariable Integer id,
      @RequestBody UpdateProductDto dto) {
    ProductDto updatedProduct = productService.updateProduct(id, dto);
    return ResponseEntity.ok(updatedProduct);
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteProduct(@PathVariable Integer id) {
    boolean deleted = productService.deleteProduct(id);
    return deleted ? ResponseEntity.noContent().build()
        : ResponseEntity.notFound().build();
  }

}