package com.shop_api.backend.entity;

import java.math.BigDecimal;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "order_items")
public class OrderItem {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  @Column(name = "order_id", nullable = false)
  private Integer orderId;

 
  @Column(name = "product_id", nullable = false)
  private Integer productId;

  @Column(name = "quantity", nullable = false)
  private Integer quantity;

  
  @Column(name = "unit_price", nullable = false, precision = 10, scale = 2)
  private BigDecimal unitPrice;

  @Column(name = "total_price", nullable = false, precision = 10, scale = 2)
  private BigDecimal totalPrice;

  @Column(name = "product_name", length = 255)
  private String productName;
}