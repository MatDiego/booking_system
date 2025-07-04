package com.example.booking_system.dto.response;

import com.example.booking_system.entity.enums.EventStatus;
import com.example.booking_system.entity.enums.EventType;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;


public record EventResponseDto(
        UUID id,
        String title,
        String description,
        LocalDateTime startDate,
        LocalDateTime endDate,
        String location,
        Integer capacity,
        EventType eventType,
        BigDecimal price,
        UserResponseDto userResponseDto,
        EventStatus eventStatus
) {
}
