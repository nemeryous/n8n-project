package com.shop_api.backend.entity;

import java.math.BigDecimal;
import java.time.Instant;

import com.shop_api.backend.constant.OrderStatus;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Order entity for checkout module - n8n workflow automation
 * Uses only ID mapping without ORM relationships as requested
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "orders")
public class Order {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  /**
   * Reference to customer - using ID only (no ORM mapping)
   */
  @Column(name = "customer_id", nullable = false)
  private Integer customerId;

  /**
   * Reference to cart - using ID only (no ORM mapping)
   * Used to track which cart was converted to this order
   */
  @Column(name = "cart_id")
  private Integer cartId;

  @Column(name = "order_date", nullable = false)
  private Instant orderDate;

  /**
   * Order status - ABANDONED status for n8n workflow triggers
   */
  @Enumerated(EnumType.STRING)
  @Column(name = "status", nullable = false)
  private OrderStatus status;

  @Column(name = "total_amount", nullable = false, precision = 10, scale = 2)
  private BigDecimal totalAmount;

  @Column(name = "shipping_address", length = 500)
  private String shippingAddress;

  @Column(name = "phone_number", length = 20)
  private String phoneNumber;

  @Column(name = "notes", length = 1000)
  private String notes;

  /**
   * Timestamp when order was abandoned (for n8n workflow tracking)
   */
  @Column(name = "abandoned_at")
  private Instant abandonedAt;

  @Column(name = "created_at")
  private Instant createdAt;

  @Column(name = "updated_at")
  private Instant updatedAt;
}