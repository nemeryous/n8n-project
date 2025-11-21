package com.shop_api.backend.repository;

import java.util.List;
import com.shop_api.backend.entity.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Integer> {
    List<CartItem> findByCustomerId(Integer customerId);

    List<CartItem> findByProductId(Integer productId);

    List<CartItem> findByCartId(Integer cartId);

    java.util.Optional<CartItem> findByCartIdAndProductId(Integer cartId, Integer productId);
}
