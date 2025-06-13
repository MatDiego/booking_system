package com.example.booking_system.dto.filter;

import com.example.booking_system.entity.enums.EventType;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Size;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record EventFilterDto(
        EventType eventType,
        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
        LocalDateTime dateFrom,
        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
        LocalDateTime dateTo,
        @Size(max = 255, message = "Location cannot exceed 255 characters")
        String location,
        @DecimalMin("0.0")
        BigDecimal priceMin,
        BigDecimal priceMax,
        UUID organizerId,
        @Size(max = 255, message = "Title cannot exceed 255 characters")
        String title
) {
}
