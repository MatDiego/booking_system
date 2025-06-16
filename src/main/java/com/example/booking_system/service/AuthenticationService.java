package com.example.booking_system.service;

import com.example.booking_system.dto.request.AuthenticationRequestDto;
import com.example.booking_system.dto.request.RegisterRequestDto;
import com.example.booking_system.dto.response.AuthenticationResponseDto;
import jakarta.validation.Valid;

public interface AuthenticationService {
    AuthenticationResponseDto register(@Valid RegisterRequestDto request);
    AuthenticationResponseDto authenticate(@Valid AuthenticationRequestDto request);
}
