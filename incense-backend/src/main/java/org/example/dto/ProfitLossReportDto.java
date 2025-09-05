
// src/main/java/com/ecommerce/sellerapp/dto/ProfitLossReportDto.java
package org.example.dto;

import java.math.BigDecimal;
import java.util.List;

public class ProfitLossReportDto {
    private BigDecimal totalRevenue;
    private BigDecimal totalCost;
    private BigDecimal totalProfit;
    private BigDecimal profitMargin;
    private List<ItemProfitDto> itemProfits;
    private int year;

    // Constructors, Getters and Setters
    public ProfitLossReportDto() {}

    public BigDecimal getTotalRevenue() { return totalRevenue; }
    public void setTotalRevenue(BigDecimal totalRevenue) { this.totalRevenue = totalRevenue; }

    public BigDecimal getTotalCost() { return totalCost; }
    public void setTotalCost(BigDecimal totalCost) { this.totalCost = totalCost; }

    public BigDecimal getTotalProfit() { return totalProfit; }
    public void setTotalProfit(BigDecimal totalProfit) { this.totalProfit = totalProfit; }

    public BigDecimal getProfitMargin() { return profitMargin; }
    public void setProfitMargin(BigDecimal profitMargin) { this.profitMargin = profitMargin; }

    public List<ItemProfitDto> getItemProfits() { return itemProfits; }
    public void setItemProfits(List<ItemProfitDto> itemProfits) { this.itemProfits = itemProfits; }

    public int getYear() { return year; }
    public void setYear(int year) { this.year = year; }
}
