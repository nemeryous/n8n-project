package com.shop_api.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.shop_api.backend.entity.CartItem;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Integer> {
  
}