package com.shop_api.backend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.shop_api.backend.service.cart.CartService;


@RestController
@RequestMapping("${api.prefix}/carts")
public class CartController {

  @Autowired
  private CartService cartService;
  
}