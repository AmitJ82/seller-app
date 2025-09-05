// src/main/java/com/ecommerce/sellerapp/controller/ReportController.java
package org.example.controller;

import org.example.dto.ProfitLossReportDto;
import org.example.services.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/reports")
@CrossOrigin(origins = "*")
public class ReportController {
    
    @Autowired
    private ReportService reportService;

    @GetMapping("/profit-loss/{year}")
    @PreAuthorize("hasRole('ADMIN')")
    public ProfitLossReportDto getProfitLossReport(@PathVariable int year) {
        return reportService.generateProfitLossReport(year);
    }
}