package com.shop_api.backend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.shop_api.backend.service.product.ProductService;

@RestController
@RequestMapping("${api.prefix}/products")
public class ProductController {

  @Autowired
  private ProductService productService;

}