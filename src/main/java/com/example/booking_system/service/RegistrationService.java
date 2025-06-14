package com.example.booking_system.service;

import com.example.booking_system.dto.response.RegistrationResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface RegistrationService {
    RegistrationResponseDto registerToEvent(UUID eventId);

    Page<RegistrationResponseDto> getUserRegistrations(Pageable pageable);

    RegistrationResponseDto cancelRegistrationByUser(UUID registrationId);
}
