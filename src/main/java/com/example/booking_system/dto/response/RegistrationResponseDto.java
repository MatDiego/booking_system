package com.example.booking_system.dto.response;

import com.example.booking_system.entity.enums.RegistrationStatus;

import java.time.LocalDateTime;
import java.util.UUID;

public record RegistrationResponseDto(
        UUID id,
        UserResponseDto user,
        EventResponseDto event,
        LocalDateTime registrationDate,
        RegistrationStatus status
) {}
