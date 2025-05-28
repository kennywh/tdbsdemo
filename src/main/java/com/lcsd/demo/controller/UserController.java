package com.lcsd.demo.controller;

import com.lcsd.demo.dto.ApiResponse;
import com.lcsd.demo.dto.UserTestData;
import com.lcsd.demo.service.TestDataService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final TestDataService testDataService;

    @GetMapping("/random")
    public ResponseEntity<ApiResponse<UserTestData>> getRandomUser() {
        log.info("GET /api/users/random - Generating random user data");
        
        try {
            UserTestData user = testDataService.generateRandomUser();
            log.info("Successfully generated random user: ID={}, Name={}", user.getId(), user.getName());
            
            ApiResponse<UserTestData> response = ApiResponse.success(user, "Random user data generated successfully");
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            log.error("Error generating random user data", e);
            ApiResponse<UserTestData> response = ApiResponse.error("Failed to generate random user data: " + e.getMessage());
            return ResponseEntity.internalServerError().body(response);
        }
    }

    @GetMapping("/list/{count}")
    public ResponseEntity<ApiResponse<List<UserTestData>>> getRandomUsers(@PathVariable int count) {
        log.info("GET /api/users/list/{} - Generating {} random users", count, count);
        
        try {
            if (count <= 0 || count > 100) {
                log.warn("Invalid count requested: {}. Must be between 1 and 100", count);
                ApiResponse<List<UserTestData>> response = ApiResponse.error("Count must be between 1 and 100");
                return ResponseEntity.badRequest().body(response);
            }
            
            List<UserTestData> users = new ArrayList<>();
            for (int i = 0; i < count; i++) {
                users.add(testDataService.generateRandomUser());
            }
            
            log.info("Successfully generated {} random users", users.size());
            ApiResponse<List<UserTestData>> response = ApiResponse.success(users, 
                String.format("Generated %d random users successfully", count));
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            log.error("Error generating {} random users", count, e);
            ApiResponse<List<UserTestData>> response = ApiResponse.error("Failed to generate random users: " + e.getMessage());
            return ResponseEntity.internalServerError().body(response);
        }
    }

    @PostMapping("/generate")
    public ResponseEntity<ApiResponse<UserTestData>> createRandomUser(@RequestBody(required = false) Map<String, Object> request) {
        log.info("POST /api/users/generate - Creating random user with request: {}", request);
        
        try {
            UserTestData user = testDataService.generateRandomUser();
            
            // If request contains preferences, we could modify the generation (for demo purposes)
            if (request != null && request.containsKey("preferredStatus")) {
                String preferredStatus = (String) request.get("preferredStatus");
                user.setStatus(preferredStatus);
                log.info("Applied preferred status: {}", preferredStatus);
            }
            
            log.info("Successfully created random user via POST: ID={}, Name={}", user.getId(), user.getName());
            ApiResponse<UserTestData> response = ApiResponse.success(user, "Random user created successfully via POST");
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            log.error("Error creating random user via POST", e);
            ApiResponse<UserTestData> response = ApiResponse.error("Failed to create random user: " + e.getMessage());
            return ResponseEntity.internalServerError().body(response);
        }
    }

    @PostMapping("/batch")
    public ResponseEntity<ApiResponse<List<UserTestData>>> createBatchUsers(@RequestBody Map<String, Object> request) {
        log.info("POST /api/users/batch - Creating batch users with request: {}", request);
        
        try {
            int count = (Integer) request.getOrDefault("count", 5);
            String preferredStatus = (String) request.get("preferredStatus");
            
            if (count <= 0 || count > 50) {
                log.warn("Invalid batch count requested: {}. Must be between 1 and 50", count);
                ApiResponse<List<UserTestData>> response = ApiResponse.error("Count must be between 1 and 50");
                return ResponseEntity.badRequest().body(response);
            }
            
            List<UserTestData> users = new ArrayList<>();
            for (int i = 0; i < count; i++) {
                UserTestData user = testDataService.generateRandomUser();
                if (preferredStatus != null) {
                    user.setStatus(preferredStatus);
                }
                users.add(user);
            }
            
            log.info("Successfully created {} users via POST batch", users.size());
            ApiResponse<List<UserTestData>> response = ApiResponse.success(users, 
                String.format("Created %d users successfully via POST batch", count));
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            log.error("Error creating batch users via POST", e);
            ApiResponse<List<UserTestData>> response = ApiResponse.error("Failed to create batch users: " + e.getMessage());
            return ResponseEntity.internalServerError().body(response);
        }
    }
} 