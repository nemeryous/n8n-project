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

/**
 * OrderItem entity for checkout module - n8n workflow automation
 * Uses only ID mapping without ORM relationships as requested
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "order_items")
public class OrderItem {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  /**
   * Reference to order - using ID only (no ORM mapping)
   */
  @Column(name = "order_id", nullable = false)
  private Integer orderId;

  /**
   * Reference to product - using ID only (no ORM mapping)
   */
  @Column(name = "product_id", nullable = false)
  private Integer productId;

  @Column(name = "quantity", nullable = false)
  private Integer quantity;

  /**
   * Unit price at the time of order (snapshot)
   */
  @Column(name = "unit_price", nullable = false, precision = 10, scale = 2)
  private BigDecimal unitPrice;

  /**
   * Total price for this item (quantity * unit_price)
   */
  @Column(name = "total_price", nullable = false, precision = 10, scale = 2)
  private BigDecimal totalPrice;

  /**
   * Product name at the time of order (snapshot for n8n workflows)
   */
  @Column(name = "product_name", length = 255)
  private String productName;
}