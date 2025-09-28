package com.shop_api.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.shop_api.backend.entity.Cart;

@Repository
public interface CartRepository extends JpaRepository<Cart, Integer> {
  
}