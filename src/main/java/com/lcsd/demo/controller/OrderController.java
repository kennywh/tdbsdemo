package com.lcsd.demo.controller;

import com.lcsd.demo.dto.ApiResponse;
import com.lcsd.demo.dto.OrderTestData;
import com.lcsd.demo.service.TestDataService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

    private final TestDataService testDataService;

    @GetMapping("/random")
    public ResponseEntity<ApiResponse<OrderTestData>> getRandomOrder() {
        log.info("GET /api/orders/random - Generating random order data");
        
        try {
            OrderTestData order = testDataService.generateRandomOrder();
            log.info("Successfully generated random order: ID={}, OrderNumber={}, Total={}", 
                order.getId(), order.getOrderNumber(), order.getTotalAmount());
            
            ApiResponse<OrderTestData> response = ApiResponse.success(order, "Random order data generated successfully");
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            log.error("Error generating random order data", e);
            ApiResponse<OrderTestData> response = ApiResponse.error("Failed to generate random order data: " + e.getMessage());
            return ResponseEntity.internalServerError().body(response);
        }
    }

    @GetMapping("/history/{count}")
    public ResponseEntity<ApiResponse<List<OrderTestData>>> getOrderHistory(@PathVariable int count) {
        log.info("GET /api/orders/history/{} - Generating {} random orders", count, count);
        
        try {
            if (count <= 0 || count > 500) {
                log.warn("Invalid count requested: {}. Must be between 1 and 500", count);
                ApiResponse<List<OrderTestData>> response = ApiResponse.error("Count must be between 1 and 500");
                return ResponseEntity.badRequest().body(response);
            }
            
            List<OrderTestData> orders = new ArrayList<>();
            for (int i = 0; i < count; i++) {
                orders.add(testDataService.generateRandomOrder());
            }
            
            log.info("Successfully generated {} random orders for history", orders.size());
            ApiResponse<List<OrderTestData>> response = ApiResponse.success(orders, 
                String.format("Generated %d orders for history successfully", count));
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            log.error("Error generating {} random orders for history", count, e);
            ApiResponse<List<OrderTestData>> response = ApiResponse.error("Failed to generate order history: " + e.getMessage());
            return ResponseEntity.internalServerError().body(response);
        }
    }

    @PostMapping("/place")
    public ResponseEntity<ApiResponse<OrderTestData>> placeRandomOrder(@RequestBody(required = false) Map<String, Object> request) {
        log.info("POST /api/orders/place - Placing random order with request: {}", request);
        
        try {
            OrderTestData order = testDataService.generateRandomOrder();
            
            // Apply request parameters if provided
            if (request != null) {
                if (request.containsKey("customerName")) {
                    String customerName = (String) request.get("customerName");
                    order.setCustomerName(customerName);
                    log.info("Applied customer name: {}", customerName);
                }
                
                if (request.containsKey("status")) {
                    String status = (String) request.get("status");
                    order.setStatus(status);
                    log.info("Applied order status: {}", status);
                }
                
                if (request.containsKey("minAmount")) {
                    Double minAmount = ((Number) request.get("minAmount")).doubleValue();
                    if (order.getTotalAmount().compareTo(BigDecimal.valueOf(minAmount)) < 0) {
                        order.setTotalAmount(BigDecimal.valueOf(minAmount));
                        log.info("Applied minimum amount: {}", minAmount);
                    }
                }
                
                if (request.containsKey("itemCount")) {
                    Integer itemCount = (Integer) request.get("itemCount");
                    order.setItemCount(itemCount);
                    log.info("Applied item count: {}", itemCount);
                }
            }
            
            log.info("Successfully placed random order via POST: ID={}, OrderNumber={}, Total={}", 
                order.getId(), order.getOrderNumber(), order.getTotalAmount());
            ApiResponse<OrderTestData> response = ApiResponse.success(order, "Random order placed successfully via POST");
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            log.error("Error placing random order via POST", e);
            ApiResponse<OrderTestData> response = ApiResponse.error("Failed to place random order: " + e.getMessage());
            return ResponseEntity.internalServerError().body(response);
        }
    }

    @PostMapping("/bulk-place")
    public ResponseEntity<ApiResponse<List<OrderTestData>>> placeBulkOrders(@RequestBody Map<String, Object> request) {
        log.info("POST /api/orders/bulk-place - Placing bulk orders with request: {}", request);
        
        try {
            int count = (Integer) request.getOrDefault("count", 3);
            String status = (String) request.get("status");
            String customerName = (String) request.get("customerName");
            
            if (count <= 0 || count > 50) {
                log.warn("Invalid bulk count requested: {}. Must be between 1 and 50", count);
                ApiResponse<List<OrderTestData>> response = ApiResponse.error("Count must be between 1 and 50");
                return ResponseEntity.badRequest().body(response);
            }
            
            List<OrderTestData> orders = new ArrayList<>();
            for (int i = 0; i < count; i++) {
                OrderTestData order = testDataService.generateRandomOrder();
                if (status != null) {
                    order.setStatus(status);
                }
                if (customerName != null) {
                    order.setCustomerName(customerName + " " + (i + 1));
                }
                orders.add(order);
            }
            
            log.info("Successfully placed {} orders via POST bulk", orders.size());
            ApiResponse<List<OrderTestData>> response = ApiResponse.success(orders, 
                String.format("Placed %d orders successfully via POST bulk", count));
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            log.error("Error placing bulk orders via POST", e);
            ApiResponse<List<OrderTestData>> response = ApiResponse.error("Failed to place bulk orders: " + e.getMessage());
            return ResponseEntity.internalServerError().body(response);
        }
    }
} 