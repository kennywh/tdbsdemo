package com.lcsd.demo.controller;

import com.lcsd.demo.dto.ApiResponse;
import com.lcsd.demo.service.ExternalApiService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/external")
@RequiredArgsConstructor
public class ExternalApiController {

    private final ExternalApiService externalApiService;
    
    /**
     * Get data from a random external API
     */
    @GetMapping("/random")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getRandomExternalData() {
        log.info("GET /api/external/random - Fetching data from random external API");
        
        try {
            Map<String, Object> result = externalApiService.callRandomExternalApi();
            
            log.info("Successfully fetched data from external API");
            ApiResponse<Map<String, Object>> response = ApiResponse.success(
                result, 
                "Successfully fetched data from external API"
            );
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            log.error("Error fetching data from external API", e);
            ApiResponse<Map<String, Object>> response = ApiResponse.error(
                "Failed to fetch data from external API: " + e.getMessage()
            );
            return ResponseEntity.internalServerError().body(response);
        }
    }
    
    /**
     * Get data from a specific external API endpoint with a given ID
     */
    @GetMapping("/{type}/{id}")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getSpecificExternalData(
            @PathVariable String type,
            @PathVariable int id) {
        
        log.info("GET /api/external/{}/{} - Fetching data from specific external API", type, id);
        
        try {
            Map<String, Object> result = externalApiService.callSpecificExternalApi(type, id);
            
            log.info("Successfully fetched data from external API for {}/{}", type, id);
            ApiResponse<Map<String, Object>> response = ApiResponse.success(
                result, 
                String.format("Successfully fetched %s with ID %d from external API", type, id)
            );
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            log.error("Error fetching data from external API for {}/{}", type, id, e);
            ApiResponse<Map<String, Object>> response = ApiResponse.error(
                "Failed to fetch data from external API: " + e.getMessage()
            );
            return ResponseEntity.internalServerError().body(response);
        }
    }
    
    /**
     * Get data from external API with query parameters
     */
    @GetMapping("/query")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getExternalDataWithParams(
            @RequestParam(defaultValue = "posts") String type,
            @RequestParam(defaultValue = "1") int id) {
        
        log.info("GET /api/external/query?type={}&id={} - Fetching data from external API with params", type, id);
        
        try {
            Map<String, Object> result = externalApiService.callSpecificExternalApi(type, id);
            
            log.info("Successfully fetched data from external API with params: type={}, id={}", type, id);
            ApiResponse<Map<String, Object>> response = ApiResponse.success(
                result, 
                String.format("Successfully fetched %s with ID %d from external API", type, id)
            );
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            log.error("Error fetching data from external API with params: type={}, id={}", type, id, e);
            ApiResponse<Map<String, Object>> response = ApiResponse.error(
                "Failed to fetch data from external API: " + e.getMessage()
            );
            return ResponseEntity.internalServerError().body(response);
        }
    }
} 