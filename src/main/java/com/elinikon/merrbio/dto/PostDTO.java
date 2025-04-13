package com.elinikon.merrbio.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.sql.Blob;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class PostDTO {
    private String title;
    private String description;
    private BigDecimal price;

}
