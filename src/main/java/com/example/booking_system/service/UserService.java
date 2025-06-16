package com.example.booking_system.service;

import com.example.booking_system.dto.request.RolesModificationRequestDto;
import com.example.booking_system.dto.response.UserResponseDto;
import jakarta.validation.Valid;

import java.util.UUID;

public interface UserService {
    UserResponseDto addRolesToUser(UUID userId, @Valid RolesModificationRequestDto requestDto);
}
