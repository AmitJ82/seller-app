package org.example.services;

import org.example.dto.ItemProfitDto;
import org.example.dto.ProfitLossReportDto;
import org.example.entity.Order;
import org.example.entity.OrderItem;
import org.example.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ReportService {
    @Autowired
    private OrderRepository orderRepository;

    public ProfitLossReportDto generateProfitLossReport(int year) {
        LocalDateTime startDate = LocalDateTime.of(year, 1, 1, 0, 0);
        LocalDateTime endDate = LocalDateTime.of(year, 12, 31, 23, 59);

        List<Order> orders = orderRepository.findOrdersBetweenDates(startDate, endDate);

        Map<Long, ItemProfitDto> itemProfitMap = new HashMap<>();
        BigDecimal totalRevenue = BigDecimal.ZERO;
        BigDecimal totalCost = BigDecimal.ZERO;

        for (Order order : orders) {
            for (OrderItem orderItem : order.getOrderItems()) {
                Long itemId = orderItem.getItem().getId();
                String itemName = orderItem.getItem().getName();
                Integer quantity = orderItem.getQuantity();
                BigDecimal revenue = orderItem.getTotalPrice();
                BigDecimal cost = orderItem.getItem().getCostPrice().multiply(BigDecimal.valueOf(quantity));
                BigDecimal profit = revenue.subtract(cost);

                totalRevenue = totalRevenue.add(revenue);
                totalCost = totalCost.add(cost);

                ItemProfitDto itemProfit = itemProfitMap.computeIfAbsent(itemId, k -> {
                    ItemProfitDto dto = new ItemProfitDto();
                    dto.setItemId(itemId);
                    dto.setItemName(itemName);
                    dto.setQuantitySold(0);
                    dto.setRevenue(BigDecimal.ZERO);
                    dto.setCost(BigDecimal.ZERO);
                    dto.setProfit(BigDecimal.ZERO);
                    return dto;
                });

                itemProfit.setQuantitySold(itemProfit.getQuantitySold() + quantity);
                itemProfit.setRevenue(itemProfit.getRevenue().add(revenue));
                itemProfit.setCost(itemProfit.getCost().add(cost));
                itemProfit.setProfit(itemProfit.getProfit().add(profit));
            }
        }

        BigDecimal totalProfit = totalRevenue.subtract(totalCost);
        BigDecimal profitMargin = totalRevenue.compareTo(BigDecimal.ZERO) > 0
                ? totalProfit.divide(totalRevenue, 4, RoundingMode.HALF_UP).multiply(BigDecimal.valueOf(100))
                : BigDecimal.ZERO;

        ProfitLossReportDto report = new ProfitLossReportDto();
        report.setYear(year);
        report.setTotalRevenue(totalRevenue);
        report.setTotalCost(totalCost);
        report.setTotalProfit(totalProfit);
        report.setProfitMargin(profitMargin);
        report.setItemProfits(itemProfitMap.values().stream().collect(Collectors.toList()));

        return report;
    }

}
