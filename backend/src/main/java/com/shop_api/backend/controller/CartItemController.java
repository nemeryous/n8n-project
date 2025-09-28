package com.shop_api.backend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.shop_api.backend.service.cartitem.CartItemService;


@RestController
@RequestMapping("${api.prefix}/cart-items")
public class CartItemController {

  @Autowired
  private CartItemService cartItemService;
  
}