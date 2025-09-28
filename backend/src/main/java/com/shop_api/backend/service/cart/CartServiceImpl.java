package com.shop_api.backend.service.cart;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shop_api.backend.repository.CartRepository;

@Service
public class CartServiceImpl implements CartService {

  @Autowired
  private CartRepository cartRepository;
  
}