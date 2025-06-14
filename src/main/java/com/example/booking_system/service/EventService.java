package com.example.booking_system.service;

import com.example.booking_system.dto.filter.EventFilterDto;
import com.example.booking_system.dto.request.EventRequestDto;
import com.example.booking_system.dto.response.EventResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface EventService {
    EventResponseDto createEvent(EventRequestDto eventRequestDto);
    Page<EventResponseDto> getAllEvents(Pageable pageable);
    EventResponseDto getEvent(UUID eventId);
    Page<EventResponseDto> findEventsByCriteria(EventFilterDto filter, Pageable pageable);
}
