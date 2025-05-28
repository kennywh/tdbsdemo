package com.lcsd.demo.controller;

import com.lcsd.demo.dto.ApiResponse;
import com.lcsd.demo.dto.ProductTestData;
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
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {

    private final TestDataService testDataService;

    @GetMapping("/random")
    public ResponseEntity<ApiResponse<ProductTestData>> getRandomProduct() {
        log.info("GET /api/products/random - Generating random product data");
        
        try {
            ProductTestData product = testDataService.generateRandomProduct();
            log.info("Successfully generated random product: ID={}, Name={}, Price={}", 
                product.getId(), product.getName(), product.getPrice());
            
            ApiResponse<ProductTestData> response = ApiResponse.success(product, "Random product data generated successfully");
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            log.error("Error generating random product data", e);
            ApiResponse<ProductTestData> response = ApiResponse.error("Failed to generate random product data: " + e.getMessage());
            return ResponseEntity.internalServerError().body(response);
        }
    }

    @GetMapping("/catalog/{count}")
    public ResponseEntity<ApiResponse<List<ProductTestData>>> getProductCatalog(@PathVariable int count) {
        log.info("GET /api/products/catalog/{} - Generating {} random products", count, count);
        
        try {
            if (count <= 0 || count > 200) {
                log.warn("Invalid count requested: {}. Must be between 1 and 200", count);
                ApiResponse<List<ProductTestData>> response = ApiResponse.error("Count must be between 1 and 200");
                return ResponseEntity.badRequest().body(response);
            }
            
            List<ProductTestData> products = new ArrayList<>();
            for (int i = 0; i < count; i++) {
                products.add(testDataService.generateRandomProduct());
            }
            
            log.info("Successfully generated {} random products for catalog", products.size());
            ApiResponse<List<ProductTestData>> response = ApiResponse.success(products, 
                String.format("Generated %d products for catalog successfully", count));
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            log.error("Error generating {} random products for catalog", count, e);
            ApiResponse<List<ProductTestData>> response = ApiResponse.error("Failed to generate product catalog: " + e.getMessage());
            return ResponseEntity.internalServerError().body(response);
        }
    }

    @PostMapping("/create")
    public ResponseEntity<ApiResponse<ProductTestData>> createRandomProduct(@RequestBody(required = false) Map<String, Object> request) {
        log.info("POST /api/products/create - Creating random product with request: {}", request);
        
        try {
            ProductTestData product = testDataService.generateRandomProduct();
            
            // Apply request parameters if provided
            if (request != null) {
                if (request.containsKey("category")) {
                    String category = (String) request.get("category");
                    product.setCategory(category);
                    log.info("Applied preferred category: {}", category);
                }
                
                if (request.containsKey("minPrice")) {
                    Double minPrice = ((Number) request.get("minPrice")).doubleValue();
                    if (product.getPrice().compareTo(BigDecimal.valueOf(minPrice)) < 0) {
                        product.setPrice(BigDecimal.valueOf(minPrice));
                        log.info("Applied minimum price: {}", minPrice);
                    }
                }
                
                if (request.containsKey("available")) {
                    Boolean available = (Boolean) request.get("available");
                    product.setAvailable(available);
                    log.info("Applied availability status: {}", available);
                }
            }
            
            log.info("Successfully created random product via POST: ID={}, Name={}, Price={}", 
                product.getId(), product.getName(), product.getPrice());
            ApiResponse<ProductTestData> response = ApiResponse.success(product, "Random product created successfully via POST");
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            log.error("Error creating random product via POST", e);
            ApiResponse<ProductTestData> response = ApiResponse.error("Failed to create random product: " + e.getMessage());
            return ResponseEntity.internalServerError().body(response);
        }
    }

    @PostMapping("/bulk-create")
    public ResponseEntity<ApiResponse<List<ProductTestData>>> createBulkProducts(@RequestBody Map<String, Object> request) {
        log.info("POST /api/products/bulk-create - Creating bulk products with request: {}", request);
        
        try {
            int count = (Integer) request.getOrDefault("count", 10);
            String category = (String) request.get("category");
            Boolean available = (Boolean) request.get("available");
            
            if (count <= 0 || count > 100) {
                log.warn("Invalid bulk count requested: {}. Must be between 1 and 100", count);
                ApiResponse<List<ProductTestData>> response = ApiResponse.error("Count must be between 1 and 100");
                return ResponseEntity.badRequest().body(response);
            }
            
            List<ProductTestData> products = new ArrayList<>();
            for (int i = 0; i < count; i++) {
                ProductTestData product = testDataService.generateRandomProduct();
                if (category != null) {
                    product.setCategory(category);
                }
                if (available != null) {
                    product.setAvailable(available);
                }
                products.add(product);
            }
            
            log.info("Successfully created {} products via POST bulk", products.size());
            ApiResponse<List<ProductTestData>> response = ApiResponse.success(products, 
                String.format("Created %d products successfully via POST bulk", count));
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            log.error("Error creating bulk products via POST", e);
            ApiResponse<List<ProductTestData>> response = ApiResponse.error("Failed to create bulk products: " + e.getMessage());
            return ResponseEntity.internalServerError().body(response);
        }
    }
} 