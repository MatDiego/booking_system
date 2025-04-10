package com.example.booking_system.service;

import com.example.booking_system.dto.request.AuthenticationRequestDto;
import com.example.booking_system.dto.request.RegisterRequestDto;
import com.example.booking_system.dto.response.AuthenticationResponseDto;

public interface AuthenticationService {
    AuthenticationResponseDto register(RegisterRequestDto request);
    AuthenticationResponseDto authenticate(AuthenticationRequestDto request);
}
