package com.example.booking_system.dto.response;

import lombok.Builder;

import java.util.UUID;

@Builder
public record UserResponseDto(
        UUID id,
        String firstName,
        String lastName,
        String username,
        String email
) {
}
