package com.lcsd.demo.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

@Slf4j
@Service
@RequiredArgsConstructor
public class ExternalApiService {

    private final RestTemplate restTemplate;
    
    @Value("${external.api.base-url:https://jsonplaceholder.typicode.com}")
    private String externalApiBaseUrl;
    
    private final String[] externalApiEndpoints = {
        "/posts/{id}",
        "/users/{id}",
        "/comments/{id}",
        "/albums/{id}",
        "/photos/{id}"
    };
    
    /**
     * Calls a random external API endpoint and returns the response
     */
    public Map<String, Object> callRandomExternalApi() {
        int endpointIndex = ThreadLocalRandom.current().nextInt(externalApiEndpoints.length);
        int resourceId = ThreadLocalRandom.current().nextInt(1, 11); // Random ID between 1-10
        
        String endpoint = externalApiEndpoints[endpointIndex];
        String url = externalApiBaseUrl + endpoint;
        
        log.info("Calling external API: {} with ID: {}", url, resourceId);
        
        try {
            ResponseEntity<Map> response = restTemplate.getForEntity(
                url, 
                Map.class, 
                resourceId
            );
            
            log.info("External API call successful. Status code: {}", response.getStatusCode());
            
            if (response.getBody() != null) {
                return response.getBody();
            } else {
                log.warn("External API returned null body");
                Map<String, Object> errorResponse = new HashMap<>();
                errorResponse.put("error", "External API returned null response");
                return errorResponse;
            }
            
        } catch (Exception e) {
            log.error("Error calling external API: {}", e.getMessage(), e);
            
            // Return a fallback response in case of error
            Map<String, Object> fallbackResponse = new HashMap<>();
            fallbackResponse.put("error", "Failed to call external API: " + e.getMessage());
            fallbackResponse.put("endpoint", endpoint);
            fallbackResponse.put("fallback", true);
            fallbackResponse.put("timestamp", System.currentTimeMillis());
            
            return fallbackResponse;
        }
    }
    
    /**
     * Calls a specific external API endpoint with the given ID
     */
    public Map<String, Object> callSpecificExternalApi(String endpointType, int id) {
        String endpoint;
        
        switch (endpointType.toLowerCase()) {
            case "post":
            case "posts":
                endpoint = "/posts/" + id;
                break;
            case "user":
            case "users":
                endpoint = "/users/" + id;
                break;
            case "comment":
            case "comments":
                endpoint = "/comments/" + id;
                break;
            case "album":
            case "albums":
                endpoint = "/albums/" + id;
                break;
            case "photo":
            case "photos":
                endpoint = "/photos/" + id;
                break;
            default:
                endpoint = "/posts/" + id;
        }
        
        String url = externalApiBaseUrl + endpoint;
        log.info("Calling specific external API: {}", url);
        
        try {
            ResponseEntity<Map> response = restTemplate.getForEntity(url, Map.class);
            
            log.info("External API call successful. Status code: {}", response.getStatusCode());
            
            if (response.getBody() != null) {
                return response.getBody();
            } else {
                log.warn("External API returned null body");
                Map<String, Object> errorResponse = new HashMap<>();
                errorResponse.put("error", "External API returned null response");
                return errorResponse;
            }
            
        } catch (Exception e) {
            log.error("Error calling external API: {}", e.getMessage(), e);
            
            // Return a fallback response in case of error
            Map<String, Object> fallbackResponse = new HashMap<>();
            fallbackResponse.put("error", "Failed to call external API: " + e.getMessage());
            fallbackResponse.put("endpoint", endpoint);
            fallbackResponse.put("fallback", true);
            fallbackResponse.put("timestamp", System.currentTimeMillis());
            
            return fallbackResponse;
        }
    }
} 