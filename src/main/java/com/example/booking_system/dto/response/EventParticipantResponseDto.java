package com.example.booking_system.dto.response;

import com.example.booking_system.entity.enums.RegistrationStatus;

import java.util.UUID;

public record EventParticipantResponseDto(
        UUID userId,
        String firstName,
        String lastName,
        String email,
        RegistrationStatus registrationStatus
) {
}
