package com.lcsd.demo.service;

import com.lcsd.demo.dto.OrderTestData;
import com.lcsd.demo.dto.ProductTestData;
import com.lcsd.demo.dto.UserTestData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.Random;

@Slf4j
@Service
public class TestDataService {
    
    private final Random random = new Random();
    
    private final String[] firstNames = {"John", "Jane", "Mike", "Sarah", "David", "Emily", "Chris", "Lisa", "Mark", "Anna"};
    private final String[] lastNames = {"Smith", "Johnson", "Williams", "Brown", "Jones", "Garcia", "Miller", "Davis", "Rodriguez", "Martinez"};
    private final String[] domains = {"gmail.com", "yahoo.com", "hotmail.com", "outlook.com", "test.com"};
    private final String[] statuses = {"ACTIVE", "INACTIVE", "PENDING", "SUSPENDED", "VERIFIED"};
    
    private final String[] productNames = {"Laptop", "Mouse", "Keyboard", "Monitor", "Tablet", "Phone", "Headphones", "Speaker", "Camera", "Printer"};
    private final String[] categories = {"Electronics", "Computers", "Accessories", "Mobile", "Audio", "Photography", "Gaming", "Office"};
    private final String[] descriptions = {"High quality product", "Best seller", "Premium grade", "Professional use", "Consumer friendly", "Latest technology"};
    
    private final String[] orderStatuses = {"PENDING", "CONFIRMED", "SHIPPED", "DELIVERED", "CANCELLED", "REFUNDED"};

    public UserTestData generateRandomUser() {
        log.debug("Generating random user test data");
        
        String firstName = firstNames[random.nextInt(firstNames.length)];
        String lastName = lastNames[random.nextInt(lastNames.length)];
        String email = firstName.toLowerCase() + "." + lastName.toLowerCase() + "@" + domains[random.nextInt(domains.length)];
        
        UserTestData user = new UserTestData(
            random.nextLong(1, 10000),
            firstName + " " + lastName,
            email,
            random.nextInt(0, 101),
            statuses[random.nextInt(statuses.length)],
            Math.round(random.nextDouble(1.0, 5.0) * 100.0) / 100.0
        );
        
        log.info("Generated user test data: {}", user);
        return user;
    }

    public ProductTestData generateRandomProduct() {
        log.debug("Generating random product test data");
        
        String name = productNames[random.nextInt(productNames.length)] + " " + (random.nextInt(10) + 1);
        BigDecimal price = BigDecimal.valueOf(random.nextDouble(10.0, 2000.0)).setScale(2, RoundingMode.HALF_UP);
        
        ProductTestData product = new ProductTestData(
            random.nextLong(1, 10000),
            name,
            categories[random.nextInt(categories.length)],
            price,
            random.nextInt(0, 1000),
            random.nextBoolean(),
            descriptions[random.nextInt(descriptions.length)]
        );
        
        log.info("Generated product test data: {}", product);
        return product;
    }

    public OrderTestData generateRandomOrder() {
        log.debug("Generating random order test data");
        
        String firstName = firstNames[random.nextInt(firstNames.length)];
        String lastName = lastNames[random.nextInt(lastNames.length)];
        String orderNumber = "ORD-" + System.currentTimeMillis() + "-" + random.nextInt(1000);
        BigDecimal totalAmount = BigDecimal.valueOf(random.nextDouble(25.0, 5000.0)).setScale(2, RoundingMode.HALF_UP);
        
        OrderTestData order = new OrderTestData(
            random.nextLong(1, 10000),
            orderNumber,
            random.nextLong(1, 1000),
            firstName + " " + lastName,
            totalAmount,
            orderStatuses[random.nextInt(orderStatuses.length)],
            LocalDateTime.now().minusDays(random.nextInt(30)),
            random.nextInt(1, 20)
        );
        
        log.info("Generated order test data: {}", order);
        return order;
    }
} 