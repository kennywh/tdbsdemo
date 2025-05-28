package com.lcsd.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductTestData {
    private Long id;
    private String name;
    private String category;
    private BigDecimal price;
    private int quantity;
    private boolean available;
    private String description;
} 