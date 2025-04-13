package com.elinikon.merrbio.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class PostResponse {
    private int id;
    private String title;
    private String description;
    private BigDecimal price;
    private byte[] image;
    private String userName;
    private LocalDateTime createdDate;
}
