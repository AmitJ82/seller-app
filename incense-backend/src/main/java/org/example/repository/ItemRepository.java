package org.example.repository;

import org.example.entity.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface ItemRepository extends JpaRepository<Item, Long> {
    List<Item> findByCategoryId(Long categoryId);

    @Query("SELECT i FROM Item i WHERE i.quantityInStock <= :threshold")
    List<Item> findLowStockItems(@Param("threshold") Integer threshold);
}