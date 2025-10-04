package com.shop_api.backend.repository;

import java.time.Instant;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.shop_api.backend.constant.OrderStatus;
import com.shop_api.backend.entity.Order;

/**
 * Repository for Order entity - n8n workflow automation
 * Includes specific queries for abandoned order tracking
 */
@Repository
public interface OrderRepository extends JpaRepository<Order, Integer> {

  /**
   * Find orders by customer ID
   */
  List<Order> findByCustomerId(Integer customerId);

  /**
   * Find orders by status - useful for n8n workflow triggers
   */
  List<Order> findByStatus(OrderStatus status);

  /**
   * Find abandoned orders for n8n processing
   */
  List<Order> findByStatusAndAbandonedAtIsNotNull(OrderStatus status);

  /**
   * Find orders by cart ID - to track cart to order conversion
   */
  Order findByCartId(Integer cartId);

  /**
   * Find pending orders that should be marked as abandoned
   * For n8n workflow automation - orders pending for more than specified time
   */
  @Query("SELECT o FROM Order o WHERE o.status = :status AND o.createdAt < :cutoffTime")
  List<Order> findPendingOrdersOlderThan(@Param("status") OrderStatus status, @Param("cutoffTime") Instant cutoffTime);

  /**
   * Find orders by customer and status
   */
  List<Order> findByCustomerIdAndStatus(Integer customerId, OrderStatus status);

  /**
   * Find orders created between dates - for n8n analytics
   */
  @Query("SELECT o FROM Order o WHERE o.createdAt BETWEEN :startDate AND :endDate")
  List<Order> findOrdersBetweenDates(@Param("startDate") Instant startDate, @Param("endDate") Instant endDate);

  /**
   * Find orders by multiple statuses - for n8n batch processing
   */
  List<Order> findByStatusIn(List<OrderStatus> statuses);

  /**
   * Count orders by status
   */
  long countByStatus(OrderStatus status);

  /**
   * Find recent orders for dashboard/analytics
   */
  @Query("SELECT o FROM Order o ORDER BY o.createdAt DESC")
  List<Order> findRecentOrders();
}