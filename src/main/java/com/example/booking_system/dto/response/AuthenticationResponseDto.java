package com.example.booking_system.dto.response;

import lombok.*;

@Builder
public record AuthenticationResponseDto(
        String token
) {
}
