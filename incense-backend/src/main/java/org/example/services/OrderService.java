package org.example.services;

import org.example.dto.*;
import org.example.entity.Item;
import org.example.entity.Order;
import org.example.entity.OrderItem;
import org.example.repository.ItemRepository;
import org.example.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class OrderService {
    
    @Autowired
    private OrderRepository orderRepository;
    
    @Autowired
    private ItemRepository itemRepository;
    
    @Autowired
    private ItemService itemService;
    
    @Autowired
    private EmailService emailService;

    public List<OrderDto> getAllOrders() {
        return orderRepository.findAllOrders().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }
    
    public Optional<OrderDto> getOrderById(Long id) {
        return orderRepository.findOrderWithItemsById(id)
                .map(this::convertToDto);
    }
    
    @Transactional
    public OrderDto createOrder(OrderDto orderDto) {
        Order order = new Order();
        order.setBuyerName(orderDto.getBuyerName());
        order.setBuyerMobile(orderDto.getBuyerMobile());
        order.setVenue(orderDto.getVenue());
        if (orderDto.getOrderDate() != null) {
            order.setOrderDate(orderDto.getOrderDate());
        }
        
        BigDecimal totalAmount = BigDecimal.ZERO;
        BigDecimal totalDiscount = BigDecimal.ZERO;
        
        List<OrderItem> orderItems = new ArrayList<>();
        
        for (OrderItemDto itemDto : orderDto.getOrderItems()) {
            Item item = itemRepository.findById(itemDto.getItemId())
                    .orElseThrow(() -> new RuntimeException("Item not found"));
            
            // Check stock availability
            if (item.getQuantityInStock() < itemDto.getQuantity()) {
                throw new RuntimeException("Insufficient stock for item: " + item.getName());
            }
            
            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(order);
            orderItem.setItem(item);
            orderItem.setQuantity(itemDto.getQuantity());
            orderItem.setSalePrice(itemDto.getSalePrice());
            orderItem.setDiscount(itemDto.getDiscount());
            
            BigDecimal itemTotal = itemDto.getSalePrice()
                    .multiply(BigDecimal.valueOf(itemDto.getQuantity()))
                    .subtract(itemDto.getDiscount());
            orderItem.setTotalPrice(itemTotal);
            
            orderItems.add(orderItem);
            
            totalAmount = totalAmount.add(itemDto.getSalePrice()
                    .multiply(BigDecimal.valueOf(itemDto.getQuantity())));
            totalDiscount = totalDiscount.add(itemDto.getDiscount());
            
            // Update stock
            itemService.updateStock(item.getId(), itemDto.getQuantity());
        }
        
        order.setOrderItems(orderItems);
        order.setTotalAmount(totalAmount);
        order.setTotalDiscount(totalDiscount);
        order.setFinalAmount(totalAmount.subtract(totalDiscount));
        
        Order savedOrder = orderRepository.save(order);
        // Send email after order is created
        String subject = "Order Confirmation";
        String body = "Thank you for your order, " + orderDto.getBuyerName() + ".\nOrder ID: " + savedOrder.getId() + "\nTotal: " + savedOrder.getFinalAmount();
        emailService.sendOrderEmail(
            "fixed-address@example.com", // Replace with your fixed address
            orderDto.getBuyerEmail(),
            subject,
            body
        );
        return convertToDto(savedOrder);
    }
    
    @Transactional
    public void processBulkOrder(BulkOrderDto bulkOrderDto) {
        for (BulkOrderItemDto bulkItem : bulkOrderDto.getItems()) {
            Item item = itemRepository.findById(bulkItem.getItemId())
                    .orElseThrow(() -> new RuntimeException("Item not found"));
            
            if (item.getQuantityInStock() < bulkItem.getQuantity()) {
                throw new RuntimeException("Insufficient stock for item: " + item.getName());
            }
            
            // Create a simple order for bulk processing
            Order order = new Order();
            order.setBuyerName("Bulk Order");
            order.setBuyerMobile("N/A");
            order.setVenue("Bulk");
            order.setOrderDate(bulkOrderDto.getOrderDate());
            
            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(order);
            orderItem.setItem(item);
            orderItem.setQuantity(bulkItem.getQuantity());
            orderItem.setSalePrice(item.getSellingPrice());
            orderItem.setDiscount(BigDecimal.ZERO);
            
            BigDecimal itemTotal = item.getSellingPrice()
                    .multiply(BigDecimal.valueOf(bulkItem.getQuantity()));
            orderItem.setTotalPrice(itemTotal);
            
            List<OrderItem> orderItems = new ArrayList<>();
            orderItems.add(orderItem);
            order.setOrderItems(orderItems);
            order.setTotalAmount(itemTotal);
            order.setTotalDiscount(BigDecimal.ZERO);
            order.setFinalAmount(itemTotal);
            
            orderRepository.save(order);
            
            // Update stock
            itemService.updateStock(item.getId(), bulkItem.getQuantity());
        }
    }
    
    public List<OrderDto> getOrdersBetweenDates(LocalDateTime startDate, LocalDateTime endDate) {
        return orderRepository.findOrdersBetweenDates(startDate, endDate).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }
    
    private OrderDto convertToDto(Order order) {
        OrderDto dto = new OrderDto();
        dto.setId(order.getId());
        dto.setBuyerName(order.getBuyerName());
        dto.setBuyerMobile(order.getBuyerMobile());
        dto.setOrderDate(order.getOrderDate());
        dto.setVenue(order.getVenue());
        dto.setTotalAmount(order.getTotalAmount());
        dto.setTotalDiscount(order.getTotalDiscount());
        dto.setFinalAmount(order.getFinalAmount());
        
        List<OrderItemDto> orderItemDtos = order.getOrderItems().stream()
                .map(this::convertOrderItemToDto)
                .collect(Collectors.toList());
        dto.setOrderItems(orderItemDtos);
        
        return dto;
    }
    
    private OrderItemDto convertOrderItemToDto(OrderItem orderItem) {
        OrderItemDto dto = new OrderItemDto();
        dto.setId(orderItem.getId());
        dto.setItemId(orderItem.getItem().getId());
        dto.setItemName(orderItem.getItem().getName());
        dto.setQuantity(orderItem.getQuantity());
        dto.setSalePrice(orderItem.getSalePrice());
        dto.setDiscount(orderItem.getDiscount());
        dto.setTotalPrice(orderItem.getTotalPrice());
        return dto;
    }
}