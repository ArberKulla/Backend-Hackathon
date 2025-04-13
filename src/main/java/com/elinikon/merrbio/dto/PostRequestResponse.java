package com.elinikon.merrbio.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PostRequestResponse {
    private int id;
    private String description;
    private String userName;
    private String phoneNumber;
    private LocalDateTime createdDate;
    private boolean approved;
}
