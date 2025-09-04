
// src/main/java/com/ecommerce/sellerapp/dto/ItemDto.java
package org.example.dto;

import java.math.BigDecimal;

public class ItemDto {
    private Long id;
    private String label;
    private String name;
    private String description;
    private String image;
    private String httpLink;
    private BigDecimal costPrice;
    private BigDecimal sellingPrice;
    private BigDecimal salePrice;

    public BigDecimal getSalePrice() {
        return salePrice;
    }

    public void setSalePrice(BigDecimal salePrice) {
        this.salePrice = salePrice;
    }

    private Integer quantityInStock;
    private Long categoryId;
    private String categoryName;
    
    // Constructors, Getters and Setters
    public ItemDto() {}
    
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
    
    public Long getCategoryId() { return categoryId; }
    public void setCategoryId(Long categoryId) { this.categoryId = categoryId; }
    
    public String getCategoryName() { return categoryName; }
    public void setCategoryName(String categoryName) { this.categoryName = categoryName; }
}