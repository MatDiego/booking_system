package com.example.booking_system.service;

import com.example.booking_system.dto.request.EventRequestDto;
import com.example.booking_system.dto.response.EventResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;
import java.util.UUID;

public interface EventService {
    EventResponseDto createEvent(EventRequestDto eventRequestDto, UUID organizerId);
    Page<EventResponseDto> getAllEvents(Pageable pageable);
    EventResponseDto getEvent(UUID eventId);
}
