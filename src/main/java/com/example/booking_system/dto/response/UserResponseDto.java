package com.example.booking_system.dto.response;

import lombok.*;

import java.util.Set;
import java.util.UUID;

@Builder
public record UserResponseDto(
        UUID id,
        String username,
        String firstName,
        String lastName,
        Set<String> roles
) {
}
