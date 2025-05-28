package com.lcsd.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserTestData {
    private Long id;
    private String name;
    private String email;
    private int score;
    private String status;
    private double rating;
} 