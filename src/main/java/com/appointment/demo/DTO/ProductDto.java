package com.appointment.demo.DTO;
import java.math.BigDecimal;
import java.time.LocalDateTime;
public class ProductDto {
    public record Request(
            String name,
            String description,
            BigDecimal price,
            Integer stock
    ) {}
    public record Response(
            Long id,
            String name,
            String description,
            BigDecimal price,
            Integer stock,
            boolean active,
            LocalDateTime createdAt,
            LocalDateTime updatedAt
    ) {}
}