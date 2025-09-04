// src/main/java/com/ecommerce/sellerapp/controller/ItemController.java
package org.example.controller;

import org.example.dto.ItemDto;
import org.example.services.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/items")
@CrossOrigin(origins = "*")
public class ItemController {
    
    @Autowired
    private ItemService itemService;
    
    @GetMapping
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public List<ItemDto> getAllItems() {
        return itemService.getAllItems();
    }
    
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<ItemDto> getItemById(@PathVariable Long id) {
        return itemService.getItemById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping("/category/{categoryId}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public List<ItemDto> getItemsByCategory(@PathVariable Long categoryId) {
        return itemService.getItemsByCategory(categoryId);
    }
    
    @GetMapping("/low-stock")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public List<ItemDto> getLowStockItems(@RequestParam(defaultValue = "10") Integer threshold) {
        return itemService.getLowStockItems(threshold);
    }
    
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ItemDto createItem(@RequestBody ItemDto itemDto) {
        return itemService.createItem(itemDto);
    }
    
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ItemDto> updateItem(@PathVariable Long id, @RequestBody ItemDto itemDto) {
        try {
            ItemDto updatedItem = itemService.updateItem(id, itemDto);
            return ResponseEntity.ok(updatedItem);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteItem(@PathVariable Long id) {
        itemService.deleteItem(id);
        return ResponseEntity.ok().build();
    }
}