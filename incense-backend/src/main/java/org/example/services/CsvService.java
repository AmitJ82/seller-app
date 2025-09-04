package org.example.services;

import org.example.dto.ItemDto;
import org.example.dto.OrderDto;
import org.example.dto.OrderItemDto;
import org.example.entity.Category;
import org.example.entity.Item;
import org.example.dto.CsvUploadResponseDto;
import org.example.repository.CategoryRepository;
import org.example.repository.ItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
public class CsvService {
    
    @Autowired
    private ItemService itemService;
    
    @Autowired
    private OrderService orderService;
    
    @Autowired
    private CategoryRepository categoryRepository;
    
    @Autowired
    private ItemRepository itemRepository;
    
    public CsvUploadResponseDto uploadItems(MultipartFile file) {
        List<String> errors = new ArrayList<>();
        int totalRows = 0;
        int successfulRows = 0;
        int failedRows = 0;
        
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
            String line;
            boolean isFirstLine = true;
            
            while ((line = reader.readLine()) != null) {
                if (isFirstLine) {
                    isFirstLine = false;
                    continue; // Skip header
                }
                
                totalRows++;
                String[] values = parseCsvLine(line);
                
                try {
                    if (values.length < 8) {
                        throw new Exception("Insufficient columns. Expected: label, name, description, image, httpLink, costPrice, sellingPrice, quantityInStock, categoryId");
                    }
                    
                    ItemDto itemDto = new ItemDto();
                    itemDto.setLabel(values[0].trim());
                    itemDto.setName(values[1].trim());
                    itemDto.setDescription(values[2].trim());
                    itemDto.setImage(values[3].trim());
                    itemDto.setHttpLink(values[4].trim());
                    itemDto.setCostPrice(new BigDecimal(values[5].trim()));
                    itemDto.setSellingPrice(new BigDecimal(values[6].trim()));
                    itemDto.setQuantityInStock(Integer.parseInt(values[7].trim()));
                    
                    // Handle category - either by ID or by name
                    if (values.length > 8 && !values[8].trim().isEmpty()) {
                        String categoryValue = values[8].trim();
                        Long categoryId = findCategoryId(categoryValue);
                        if (categoryId != null) {
                            itemDto.setCategoryId(categoryId);
                        } else {
                            throw new Exception("Category not found: " + categoryValue);
                        }
                    }
                    
                    itemService.createItem(itemDto);
                    successfulRows++;
                    
                } catch (Exception e) {
                    failedRows++;
                    errors.add("Row " + totalRows + ": " + e.getMessage());
                }
            }
            
        } catch (Exception e) {
            errors.add("File processing error: " + e.getMessage());
        }
        
        String message = String.format("Processed %d rows. %d successful, %d failed.", 
                                     totalRows, successfulRows, failedRows);
        
        return new CsvUploadResponseDto(totalRows, successfulRows, failedRows, errors, message);
    }
    
    public CsvUploadResponseDto uploadOrders(MultipartFile file) {
        List<String> errors = new ArrayList<>();
        int totalRows = 0;
        int successfulRows = 0;
        int failedRows = 0;
        
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
            String line;
            boolean isFirstLine = true;
            
            while ((line = reader.readLine()) != null) {
                if (isFirstLine) {
                    isFirstLine = false;
                    continue; // Skip header
                }
                
                totalRows++;
                String[] values = parseCsvLine(line);
                
                try {
                    if (values.length < 6) {
                        throw new Exception("Insufficient columns. Expected: buyerName, buyerMobile, venue, orderDate, itemId, quantity, salePrice(optional), discount(optional)");
                    }
                    
                    OrderDto orderDto = new OrderDto();
                    orderDto.setBuyerName(values[0].trim());
                    orderDto.setBuyerMobile(values[1].trim());
                    orderDto.setVenue(values[2].trim());
                    
                    // Parse order date
                    try {
                        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                        orderDto.setOrderDate(LocalDateTime.parse(values[3].trim(), formatter));
                    } catch (DateTimeParseException e) {
                        // Try alternative format
                        DateTimeFormatter altFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                        orderDto.setOrderDate(LocalDateTime.parse(values[3].trim() + " 00:00:00", 
                                DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
                    }
                    
                    // Parse order items
                    List<OrderItemDto> orderItems = new ArrayList<>();
                    OrderItemDto orderItem = new OrderItemDto();
                    
                    Long itemId = Long.parseLong(values[4].trim());
                    Item item = itemRepository.findById(itemId)
                            .orElseThrow(() -> new Exception("Item not found with ID: " + itemId));
                    
                    orderItem.setItemId(itemId);
                    orderItem.setQuantity(Integer.parseInt(values[5].trim()));
                    
                    // Sale price (optional, defaults to item selling price)
                    if (values.length > 6 && !values[6].trim().isEmpty()) {
                        orderItem.setSalePrice(new BigDecimal(values[6].trim()));
                    } else {
                        orderItem.setSalePrice(item.getSellingPrice());
                    }
                    
                    // Discount (optional, defaults to 0)
                    if (values.length > 7 && !values[7].trim().isEmpty()) {
                        orderItem.setDiscount(new BigDecimal(values[7].trim()));
                    } else {
                        orderItem.setDiscount(BigDecimal.ZERO);
                    }
                    
                    orderItems.add(orderItem);
                    orderDto.setOrderItems(orderItems);
                    
                    orderService.createOrder(orderDto);
                    successfulRows++;
                    
                } catch (Exception e) {
                    failedRows++;
                    errors.add("Row " + totalRows + ": " + e.getMessage());
                }
            }
            
        } catch (Exception e) {
            errors.add("File processing error: " + e.getMessage());
        }
        
        String message = String.format("Processed %d rows. %d successful, %d failed.", 
                                     totalRows, successfulRows, failedRows);
        
        return new CsvUploadResponseDto(totalRows, successfulRows, failedRows, errors, message);
    }
    
    private Long findCategoryId(String categoryValue) {
        // Try to parse as ID first
        try {
            Long categoryId = Long.parseLong(categoryValue);
            if (categoryRepository.existsById(categoryId)) {
                return categoryId;
            }
        } catch (NumberFormatException e) {
            // Not a number, try to find by name
        }
        
        // Find by name
        Optional<Category> category = categoryRepository.findAll()
                .stream()
                .filter(c -> c.getName().equalsIgnoreCase(categoryValue) || 
                           c.getLabel().equalsIgnoreCase(categoryValue))
                .findFirst();
        
        return category.map(Category::getId).orElse(null);
    }
    
    private String[] parseCsvLine(String line) {
        List<String> result = new ArrayList<>();
        boolean inQuotes = false;
        StringBuilder currentValue = new StringBuilder();
        
        for (int i = 0; i < line.length(); i++) {
            char ch = line.charAt(i);
            
            if (ch == '"') {
                inQuotes = !inQuotes;
            } else if (ch == ',' && !inQuotes) {
                result.add(currentValue.toString());
                currentValue.setLength(0);
            } else {
                currentValue.append(ch);
            }
        }
        
        result.add(currentValue.toString());
        return result.toArray(new String[0]);
    }
    
    public String generateItemsTemplate() {
        return "label,name,description,image,httpLink,costPrice,sellingPrice,quantityInStock,categoryId\n" +
               "SAMPLE_ITEM,Sample Item,Sample description,sample.jpg,http://example.com/sample,100.00,150.00,50,1\n" +
               "ANOTHER_ITEM,Another Item,Another description,another.jpg,http://example.com/another,200.00,250.00,25,2";
    }
    
    public String generateOrdersTemplate() {
        return "buyerName,buyerMobile,venue,orderDate,itemId,quantity,salePrice,discount\n" +
               "John Doe,9876543210,Store A,2024-01-15 10:30:00,1,2,150.00,10.00\n" +
               "Jane Smith,9876543211,Store B,2024-01-15 14:20:00,2,1,250.00,0.00";
    }
}