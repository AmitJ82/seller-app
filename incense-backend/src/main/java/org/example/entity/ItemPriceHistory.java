package org.example.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "item_price_history")
public class ItemPriceHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    private Item item;
    
    @Column(name = "old_cost_price", precision = 10, scale = 2)
    private BigDecimal oldCostPrice;
    
    @Column(name = "new_cost_price", precision = 10, scale = 2)
    private BigDecimal newCostPrice;
    
    @Column(name = "old_selling_price", precision = 10, scale = 2)
    private BigDecimal oldSellingPrice;
    
    @Column(name = "new_selling_price", precision = 10, scale = 2)
    private BigDecimal newSellingPrice;
    
    @Column(name = "change_date")
    private LocalDateTime changeDate = LocalDateTime.now();
    
    // Constructors, Getters and Setters
    public ItemPriceHistory() {}
    
    public ItemPriceHistory(Item item, BigDecimal oldCostPrice, BigDecimal newCostPrice, 
                           BigDecimal oldSellingPrice, BigDecimal newSellingPrice) {
        this.item = item;
        this.oldCostPrice = oldCostPrice;
        this.newCostPrice = newCostPrice;
        this.oldSellingPrice = oldSellingPrice;
        this.newSellingPrice = newSellingPrice;
    }
    
    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public Item getItem() { return item; }
    public void setItem(Item item) { this.item = item; }
    
    public BigDecimal getOldCostPrice() { return oldCostPrice; }
    public void setOldCostPrice(BigDecimal oldCostPrice) { this.oldCostPrice = oldCostPrice; }
    
    public BigDecimal getNewCostPrice() { return newCostPrice; }
    public void setNewCostPrice(BigDecimal newCostPrice) { this.newCostPrice = newCostPrice; }
    
    public BigDecimal getOldSellingPrice() { return oldSellingPrice; }
    public void setOldSellingPrice(BigDecimal oldSellingPrice) { this.oldSellingPrice = oldSellingPrice; }
    
    public BigDecimal getNewSellingPrice() { return newSellingPrice; }
    public void setNewSellingPrice(BigDecimal newSellingPrice) { this.newSellingPrice = newSellingPrice; }
    
    public LocalDateTime getChangeDate() { return changeDate; }
    public void setChangeDate(LocalDateTime changeDate) { this.changeDate = changeDate; }
}