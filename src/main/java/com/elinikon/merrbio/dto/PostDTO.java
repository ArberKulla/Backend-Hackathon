package com.elinikon.merrbio.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class PostDTO {
    private String title;
    private String description;
    private String price;
    private byte[] image;

}
