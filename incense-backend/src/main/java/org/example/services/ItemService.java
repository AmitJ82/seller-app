
// src/main/java/com/ecommerce/sellerapp/service/ItemService.java
package org.example.services;

import org.example.dto.ItemDto;
import org.example.entity.Category;
import org.example.entity.Item;
import org.example.entity.ItemPriceHistory;
import org.example.repository.CategoryRepository;
import org.example.repository.ItemPriceHistoryRepository;
import org.example.repository.ItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ItemService {
    
    @Autowired
    private ItemRepository itemRepository;
    
    @Autowired
    private CategoryRepository categoryRepository;
    
    @Autowired
    private ItemPriceHistoryRepository priceHistoryRepository;
    
    public List<ItemDto> getAllItems() {
        return itemRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }
    
    public List<ItemDto> getItemsByCategory(Long categoryId) {
        return itemRepository.findByCategoryId(categoryId).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }
    
    public Optional<ItemDto> getItemById(Long id) {
        return itemRepository.findById(id)
                .map(this::convertToDto);
    }
    
    public ItemDto createItem(ItemDto itemDto) {
        Category category = categoryRepository.findById(itemDto.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Category not found"));
        
        Item item = convertToEntity(itemDto, category);
        Item savedItem = itemRepository.save(item);
        return convertToDto(savedItem);
    }
    
    public ItemDto updateItem(Long id, ItemDto itemDto) {
        Item item = itemRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Item not found"));
        
        // Track price changes
        if (!item.getCostPrice().equals(itemDto.getCostPrice()) || 
            !item.getSellingPrice().equals(itemDto.getSellingPrice())) {
            
            ItemPriceHistory priceHistory = new ItemPriceHistory(
                item, 
                item.getCostPrice(), 
                itemDto.getCostPrice(),
                item.getSellingPrice(), 
                itemDto.getSellingPrice()
            );
            priceHistoryRepository.save(priceHistory);
        }
        
        Category category = categoryRepository.findById(itemDto.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Category not found"));
        
        item.setLabel(itemDto.getLabel());
        item.setName(itemDto.getName());
        item.setDescription(itemDto.getDescription());
        item.setImage(itemDto.getImage());
        item.setHttpLink(itemDto.getHttpLink());
        item.setCostPrice(itemDto.getCostPrice());
        item.setSellingPrice(itemDto.getSellingPrice());
        item.setQuantityInStock(itemDto.getQuantityInStock());
        item.setCategory(category);
        
        Item savedItem = itemRepository.save(item);
        return convertToDto(savedItem);
    }
    
    public void deleteItem(Long id) {
        itemRepository.deleteById(id);
    }
    
    public void updateStock(Long itemId, Integer quantity) {
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new RuntimeException("Item not found"));
        
        if (item.getQuantityInStock() < quantity) {
            throw new RuntimeException("Insufficient stock");
        }
        
        item.setQuantityInStock(item.getQuantityInStock() - quantity);
        itemRepository.save(item);
    }
    
    public List<ItemDto> getLowStockItems(Integer threshold) {
        return itemRepository.findLowStockItems(threshold).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }
    
    private ItemDto convertToDto(Item item) {
        ItemDto dto = new ItemDto();
        dto.setId(item.getId());
        dto.setLabel(item.getLabel());
        dto.setName(item.getName());
        dto.setDescription(item.getDescription());
        dto.setImage(item.getImage());
        dto.setHttpLink(item.getHttpLink());
        dto.setCostPrice(item.getCostPrice());
        dto.setSellingPrice(item.getSellingPrice());
        dto.setQuantityInStock(item.getQuantityInStock());
        dto.setCategoryId(item.getCategory().getId());
        dto.setCategoryName(item.getCategory().getName());
        return dto;
    }
    
    private Item convertToEntity(ItemDto dto, Category category) {
        Item item = new Item();
        item.setLabel(dto.getLabel());
        item.setName(dto.getName());
        item.setDescription(dto.getDescription());
        item.setImage(dto.getImage());
        item.setHttpLink(dto.getHttpLink());
        item.setCostPrice(dto.getCostPrice());
        item.setSellingPrice(dto.getSellingPrice());
        item.setQuantityInStock(dto.getQuantityInStock());
        item.setCategory(category);
        return item;
    }
}