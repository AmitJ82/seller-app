
// src/main/java/com/ecommerce/sellerapp/repository/ItemPriceHistoryRepository.java
package org.example.repository;

import org.example.entity.ItemPriceHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ItemPriceHistoryRepository extends JpaRepository<ItemPriceHistory, Long> {
    List<ItemPriceHistory> findByItemIdOrderByChangeDateDesc(Long itemId);
}
