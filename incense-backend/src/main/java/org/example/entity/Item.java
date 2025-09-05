
// src/main/java/com/ecommerce/sellerapp/entity/Item.java
package org.example.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "items")
public class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotBlank
    private String label;
    
    @NotBlank
    private String name;
    
    @Column(length = 1000)
    private String description;
    
    private String image;
    
    @Column(name = "http_link")
    private String httpLink;
    
    @NotNull
    @Column(name = "cost_price", precision = 10, scale = 2)
    private BigDecimal costPrice;
    
    @NotNull
    @Column(name = "selling_price", precision = 10, scale = 2)
    private BigDecimal sellingPrice;
    
    @NotNull
    @Column(name = "quantity_in_stock")
    private Integer quantityInStock = 0;
    
    @Column(name = "created_date")
    private LocalDateTime createdDate = LocalDateTime.now();
    
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "category_id")
    private Category category;
    
    @OneToMany(mappedBy = "item", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<ItemPriceHistory> priceHistory;
    
    @OneToMany(mappedBy = "item", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<OrderItem> orderItems;
    
    // Constructors
    public Item() {}
    
    public Item(String label, String name, String description, String image, String httpLink, 
                BigDecimal costPrice, BigDecimal sellingPrice, Integer quantityInStock, Category category) {
        this.label = label;
        this.name = name;
        this.description = description;
        this.image = image;
        this.httpLink = httpLink;
        this.costPrice = costPrice;
        this.sellingPrice = sellingPrice;
        this.quantityInStock = quantityInStock;
        this.category = category;
    }
    
    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public String getLabel() { return label; }
    public void setLabel(String label) { this.label = label; }
    
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    
    public String getImage() { return image; }
    public void setImage(String image) { this.image = image; }
    
    public String getHttpLink() { return httpLink; }
    public void setHttpLink(String httpLink) { this.httpLink = httpLink; }
    
    public BigDecimal getCostPrice() { return costPrice; }
    public void setCostPrice(BigDecimal costPrice) { this.costPrice = costPrice; }
    
    public BigDecimal getSellingPrice() { return sellingPrice; }
    public void setSellingPrice(BigDecimal sellingPrice) { this.sellingPrice = sellingPrice; }
    
    public Integer getQuantityInStock() { return quantityInStock; }
    public void setQuantityInStock(Integer quantityInStock) { this.quantityInStock = quantityInStock; }
    
    public LocalDateTime getCreatedDate() { return createdDate; }
    public void setCreatedDate(LocalDateTime createdDate) { this.createdDate = createdDate; }
    
    public Category getCategory() { return category; }
    public void setCategory(Category category) { this.category = category; }
    
    public List<ItemPriceHistory> getPriceHistory() { return priceHistory; }
    public void setPriceHistory(List<ItemPriceHistory> priceHistory) { this.priceHistory = priceHistory; }
    
    public List<OrderItem> getOrderItems() { return orderItems; }
    public void setOrderItems(List<OrderItem> orderItems) { this.orderItems = orderItems; }
}
