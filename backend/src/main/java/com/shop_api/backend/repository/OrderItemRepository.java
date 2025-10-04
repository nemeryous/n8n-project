package com.shop_api.backend.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.shop_api.backend.entity.OrderItem;

/**
 * Repository for OrderItem entity - n8n workflow automation
 */
@Repository
public interface OrderItemRepository extends JpaRepository<OrderItem, Integer> {

  /**
   * Find all items for a specific order
   */
  List<OrderItem> findByOrderId(Integer orderId);

  /**
   * Find items by product ID - useful for inventory tracking in n8n
   */
  List<OrderItem> findByProductId(Integer productId);

  /**
   * Find order items by order IDs - for batch processing
   */
  List<OrderItem> findByOrderIdIn(List<Integer> orderIds);

  /**
   * Count items for an order
   */
  long countByOrderId(Integer orderId);

  /**
   * Calculate total quantity for a product across all orders
   */
  @Query("SELECT SUM(oi.quantity) FROM OrderItem oi WHERE oi.productId = :productId")
  Long getTotalQuantityByProductId(@Param("productId") Integer productId);

  /**
   * Delete all items for a specific order
   */
  void deleteByOrderId(Integer orderId);

  /**
   * Find order items with order details for n8n workflows
   */
  @Query("SELECT oi FROM OrderItem oi WHERE oi.orderId IN " +
         "(SELECT o.id FROM Order o WHERE o.status = :status)")
  List<OrderItem> findItemsByOrderStatus(@Param("status") String status);

  /**
   * Get total sales amount for a product
   */
  @Query("SELECT SUM(oi.totalPrice) FROM OrderItem oi WHERE oi.productId = :productId")
  Double getTotalSalesAmountByProductId(@Param("productId") Integer productId);
}