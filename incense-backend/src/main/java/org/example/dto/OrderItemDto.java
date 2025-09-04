
// src/main/java/com/ecommerce/sellerapp/dto/OrderItemDto.java
package org.example.dto;

import java.math.BigDecimal;

public class OrderItemDto {
    private Long id;
    private Long itemId;
    private String itemName;
    private Integer quantity;
    private BigDecimal salePrice;
    private BigDecimal discount;
    private BigDecimal totalPrice;
    
    // Constructors, Getters and Setters
    public OrderItemDto() {}
    
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public Long getItemId() { return itemId; }
    public void setItemId(Long itemId) { this.itemId = itemId; }
    
    public String getItemName() { return itemName; }
    public void setItemName(String itemName) { this.itemName = itemName; }
    
    public Integer getQuantity() { return quantity; }
    public void setQuantity(Integer quantity) { this.quantity = quantity; }
    
    public BigDecimal getSalePrice() { return salePrice; }
    public void setSalePrice(BigDecimal salePrice) { this.salePrice = salePrice; }
    
    public BigDecimal getDiscount() { return discount; }
    public void setDiscount(BigDecimal discount) { this.discount = discount; }
    
    public BigDecimal getTotalPrice() { return totalPrice; }
    public void setTotalPrice(BigDecimal totalPrice) { this.totalPrice = totalPrice; }
}