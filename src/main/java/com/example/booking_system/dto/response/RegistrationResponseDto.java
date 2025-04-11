package com.example.booking_system.dto.response;

import com.example.booking_system.dto.request.EventRequestDto;
import com.example.booking_system.entity.enums.StatusType;

import java.time.LocalDateTime;
import java.util.UUID;

public record RegistrationResponseDto(
        UUID id,
        UserResponseDto user,
        EventRequestDto event,
        LocalDateTime registrationDate,
        StatusType status
) {}
