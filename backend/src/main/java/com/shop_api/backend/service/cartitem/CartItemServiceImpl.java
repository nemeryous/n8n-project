package com.shop_api.backend.service.cartitem;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shop_api.backend.repository.CartItemRepository;

@Service
public class CartItemServiceImpl implements CartItemService {

  @Autowired
  private CartItemRepository cartItemRepository;
  
}