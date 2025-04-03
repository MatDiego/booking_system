package com.example.booking_system.dto.request;

import lombok.*;


@Builder
public record AuthenticationRequestDto(
        String username,
        String password
) { }
