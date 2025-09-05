// src/main/java/com/ecommerce/sellerapp/dto/OrderDto.java
package org.example.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public class OrderDto {
    private Long id;
    private String buyerName;
    private String buyerMobile;
    private String buyerEmail;
    private LocalDateTime orderDate;
    private String venue;
    private BigDecimal totalAmount;
    private BigDecimal totalDiscount;
    private BigDecimal finalAmount;
    private List<OrderItemDto> orderItems;
    
    // Constructors, Getters and Setters
    public OrderDto() {}
    
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public String getBuyerName() { return buyerName; }
    public void setBuyerName(String buyerName) { this.buyerName = buyerName; }
    
    public String getBuyerMobile() { return buyerMobile; }
    public void setBuyerMobile(String buyerMobile) { this.buyerMobile = buyerMobile; }
    
    public String getBuyerEmail() { return buyerEmail; }
    public void setBuyerEmail(String buyerEmail) { this.buyerEmail = buyerEmail; }

    public LocalDateTime getOrderDate() { return orderDate; }
    public void setOrderDate(LocalDateTime orderDate) { this.orderDate = orderDate; }
    
    public String getVenue() { return venue; }
    public void setVenue(String venue) { this.venue = venue; }
    
    public BigDecimal getTotalAmount() { return totalAmount; }
    public void setTotalAmount(BigDecimal totalAmount) { this.totalAmount = totalAmount; }
    
    public BigDecimal getTotalDiscount() { return totalDiscount; }
    public void setTotalDiscount(BigDecimal totalDiscount) { this.totalDiscount = totalDiscount; }
    
    public BigDecimal getFinalAmount() { return finalAmount; }
    public void setFinalAmount(BigDecimal finalAmount) { this.finalAmount = finalAmount; }
    
    public List<OrderItemDto> getOrderItems() { return orderItems; }
    public void setOrderItems(List<OrderItemDto> orderItems) { this.orderItems = orderItems; }
}
