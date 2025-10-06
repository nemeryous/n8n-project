package com.shop_api.backend.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.shop_api.backend.constant.CartStatus;
import com.shop_api.backend.entity.Cart;

@Repository
public interface CartRepository extends JpaRepository<Cart, Integer> {

  List<Cart> findByCustomerId(Integer customerId);

  Optional<Cart> findByCustomerIdAndStatus(Integer customerId, CartStatus status);

  Optional<Cart> findBySessionIdAndStatus(String sessionId, CartStatus status);

  List<Cart> findByStatus(CartStatus status);
}