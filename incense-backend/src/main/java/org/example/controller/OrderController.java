package org.example.controller;

import org.example.dto.BulkOrderDto;
import org.example.dto.OrderDto;
import org.example.services.OrderService;
import org.example.services.SesEmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/orders")
@CrossOrigin(origins = "*")
public class OrderController {
    
    @Autowired
    private OrderService orderService;

    @Autowired
    private SesEmailService sesEmailService;

    @GetMapping
    //@PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public List<OrderDto> getAllOrders() {
        return orderService.getAllOrders();
    }
    
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<OrderDto> getOrderById(@PathVariable Long id) {
        return orderService.getOrderById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping("/date-range")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public List<OrderDto> getOrdersBetweenDates(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
        return orderService.getOrdersBetweenDates(startDate, endDate);
    }
    
    @PostMapping
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<OrderDto> createOrder(@RequestBody OrderDto orderDto) {
        try {
            OrderDto createdOrder = orderService.createOrder(orderDto);
            return ResponseEntity.ok(createdOrder);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    @PostMapping("/bulk")
    public ResponseEntity<String> processBulkOrder(@RequestBody BulkOrderDto bulkOrderDto) {
        try {
            orderService.processBulkOrder(bulkOrderDto);
            return ResponseEntity.ok("Bulk order processed successfully");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/sendOrderEmail")
    public ResponseEntity<String> sendOrderEmail(@RequestBody OrderDto orderDto) {
        try {
            String customerEmail = orderDto.getBuyerEmail(); // Ensure OrderDto has getEmail()
            String subject = "New Order Received";
            String body = "Order details: " + orderDto.toString(); // Customize as needed
            sesEmailService.sendOrderEmail(customerEmail, subject, body);
            return ResponseEntity.ok("Order email sent successfully");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Failed to send order email: " + e.getMessage());
        }
    }
}