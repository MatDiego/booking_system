package com.example.booking_system.dto.update;

import com.example.booking_system.entity.enums.EventStatus;
import com.example.booking_system.entity.enums.EventType;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record UpdateEventDto(
        String title,
        String description,
        LocalDateTime startDate,
        LocalDateTime endDate,
        String location,
        Integer capacity,
        EventType eventType,
        BigDecimal price,
        EventStatus eventStatus
) {
}
