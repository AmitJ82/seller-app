package org.example.controller;
import org.example.dto.CsvUploadResponseDto;
import org.example.services.CsvService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/csv")
@CrossOrigin(origins = "http://localhost:3000")
public class CsvController {
    
    @Autowired
    private CsvService csvService;
    
    @PostMapping("/upload/items")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CsvUploadResponseDto> uploadItems(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return ResponseEntity.badRequest()
                    .body(new CsvUploadResponseDto(0, 0, 0, 
                            java.util.Arrays.asList("No file provided"), "Upload failed"));
        }
        
        if (!file.getOriginalFilename().toLowerCase().endsWith(".csv")) {
            return ResponseEntity.badRequest()
                    .body(new CsvUploadResponseDto(0, 0, 0, 
                            java.util.Arrays.asList("File must be a CSV"), "Upload failed"));
        }
        
        try {
            CsvUploadResponseDto response = csvService.uploadItems(file);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body(new CsvUploadResponseDto(0, 0, 0, 
                            java.util.Arrays.asList("Error processing file: " + e.getMessage()), "Upload failed"));
        }
    }
    
    @PostMapping("/upload/orders")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CsvUploadResponseDto> uploadOrders(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return ResponseEntity.badRequest()
                    .body(new CsvUploadResponseDto(0, 0, 0, 
                            java.util.Arrays.asList("No file provided"), "Upload failed"));
        }
        
        if (!file.getOriginalFilename().toLowerCase().endsWith(".csv")) {
            return ResponseEntity.badRequest()
                    .body(new CsvUploadResponseDto(0, 0, 0, 
                            java.util.Arrays.asList("File must be a CSV"), "Upload failed"));
        }
        
        try {
            CsvUploadResponseDto response = csvService.uploadOrders(file);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body(new CsvUploadResponseDto(0, 0, 0, 
                            java.util.Arrays.asList("Error processing file: " + e.getMessage()), "Upload failed"));
        }
    }
    
    @GetMapping("/template/items")
    public ResponseEntity<String> downloadItemsTemplate() {
        String csvContent = csvService.generateItemsTemplate();
        
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType("text/csv"));
        headers.setContentDispositionFormData("attachment", "items_template.csv");
        
        return ResponseEntity.ok()
                .headers(headers)
                .body(csvContent);
    }
    
    @GetMapping("/template/orders")
    public ResponseEntity<String> downloadOrdersTemplate() {
        String csvContent = csvService.generateOrdersTemplate();
        
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType("text/csv"));
        headers.setContentDispositionFormData("attachment", "orders_template.csv");
        
        return ResponseEntity.ok()
                .headers(headers)
                .body(csvContent);
    }
}