package com.example.booking_system.dto.request;

import com.example.booking_system.entity.enums.EventStatus;
import com.example.booking_system.entity.enums.EventType;
import com.example.booking_system.validation.DateRange;
import com.example.booking_system.validation.EndDateAfterStartDate;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;


@EndDateAfterStartDate
public record EventRequestDto(
        @NotBlank(message = "Title cannot be blank")
        @Size(max = 255, message = "Title cannot exceed 255 characters")
        String title,
        @NotBlank(message = "Description cannot be blank")
        String description,
        @NotNull(message = "Start date cannot be null")
        @Future(message = "Start date must be in the future")
        LocalDateTime startDate,
        @NotNull(message = "End date cannot be null")
        @Future(message = "End date must be in the future")
        LocalDateTime endDate,
        @NotBlank(message = "Location cannot be blank")
        @Size(max = 255, message = "Location cannot exceed 255 characters")
        String location,
        @NotNull(message = "Capacity cannot be null")
        @Min(value = 1, message = "Capacity must be at least 1")
        Integer capacity,
        @NotNull(message = "Event type cannot be null")
        EventType eventType,
        @NotNull(message = "Price cannot be null")
        @PositiveOrZero(message = "Price must be positive or zero")
        BigDecimal price,
        EventStatus eventStatus
) implements DateRange {
        @Override
        public LocalDateTime getStartDate() {
                return startDate;
        }

        @Override
        public LocalDateTime getEndDate() {
                return endDate;
        }
}
