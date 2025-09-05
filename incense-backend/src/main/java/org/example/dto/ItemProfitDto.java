
// src/main/java/com/ecommerce/sellerapp/dto/ItemProfitDto.java
package org.example.dto;

import java.math.BigDecimal;

public class ItemProfitDto {
    private Long itemId;
    private String itemName;
    private Integer quantitySold;
    private BigDecimal revenue;
    private BigDecimal cost;
    private BigDecimal profit;

    // Constructors, Getters and Setters
    public ItemProfitDto() {}

    public Long getItemId() { return itemId; }
    public void setItemId(Long itemId) { this.itemId = itemId; }

    public String getItemName() { return itemName; }
    public void setItemName(String itemName) { this.itemName = itemName; }

    public Integer getQuantitySold() { return quantitySold; }
    public void setQuantitySold(Integer quantitySold) { this.quantitySold = quantitySold; }

    public BigDecimal getRevenue() { return revenue; }
    public void setRevenue(BigDecimal revenue) { this.revenue = revenue; }

    public BigDecimal getCost() { return cost; }
    public void setCost(BigDecimal cost) { this.cost = cost; }

    public BigDecimal getProfit() { return profit; }
    public void setProfit(BigDecimal profit) { this.profit = profit; }
}