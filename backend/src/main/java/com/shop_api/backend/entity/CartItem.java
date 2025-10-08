package com.shop_api.backend.entity;

import java.math.BigDecimal;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

/**
 * CartItem entity - updated with cart_id for proper ERD relationship
 * Uses only ID mapping without ORM relationships
 */
@Data
@Entity
@Table(name = "cart_items")
public class CartItem {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  @Column(name = "cart_id", nullable = false)
  private Integer cartId;

  @Column(name = "product_id")
  private Integer productId;

  @Column(name = "customer_id")
  private Integer customerId;

  @Column(name = "quantity")
  private Integer quantity;

  @Column(name = "unit_price")
  private Double unitPrice;

  @Column(name = "total_price")
  private Double totalPrice;

}
