
// src/main/java/com/ecommerce/sellerapp/dto/BulkOrderDto.java
package org.example.dto;

import java.time.LocalDateTime;
import java.util.List;

public class BulkOrderDto {
    private LocalDateTime orderDate;
    private List<BulkOrderItemDto> items;
    
    // Getters and Setters
    public LocalDateTime getOrderDate() { return orderDate; }
    public void setOrderDate(LocalDateTime orderDate) { this.orderDate = orderDate; }
    
    public List<BulkOrderItemDto> getItems() { return items; }
    public void setItems(List<BulkOrderItemDto> items) { this.items = items; }
}