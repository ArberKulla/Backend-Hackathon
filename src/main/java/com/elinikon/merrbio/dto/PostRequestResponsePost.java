package com.elinikon.merrbio.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PostRequestResponsePost {
    private int id;
    private String postTitle;
    private String postDescription;
    private BigDecimal postPrice;
    private String description;
    private String phoneNumber;
    private LocalDateTime createdDate;
    private boolean approved;
}
