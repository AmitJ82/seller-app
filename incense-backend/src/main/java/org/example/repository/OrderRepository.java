// src/main/java/com/ecommerce/sellerapp/repository/OrderRepository.java
package org.example.repository;

import org.example.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    @Query("SELECT o FROM Order o WHERE o.orderDate BETWEEN :startDate AND :endDate")
    List<Order> findOrdersBetweenDates(@Param("startDate") LocalDateTime startDate,
                                      @Param("endDate") LocalDateTime endDate);
    @Query("SELECT o FROM Order o JOIN FETCH o.orderItems JOIN FETCH o.orderItems oi JOIN FETCH oi.item order by o.id DESC")
    List<Order> findAllOrders();
    @Query("SELECT o FROM Order o JOIN FETCH o.orderItems WHERE o.id = :id")
    Optional<Order> findOrderWithItemsById(@Param("id") Long id);
}