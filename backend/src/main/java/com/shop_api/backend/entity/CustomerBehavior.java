package com.shop_api.backend.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "customer_behaviors")
public class CustomerBehavior {

  @Id
  @Column(name = "customer_id")
  private Integer customerId;

  @Column(name = "average_cart_value")
  private Double averageCartValue;

  @Column(name = "abandonment_cart_count")
  private Double abandonmentCartCount;
}
