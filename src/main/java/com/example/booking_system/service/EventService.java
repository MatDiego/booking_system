package com.example.booking_system.service;

import com.example.booking_system.dto.filter.EventFilterDto;
import com.example.booking_system.dto.request.EventRequestDto;
import com.example.booking_system.dto.response.EventParticipantResponseDto;
import com.example.booking_system.dto.response.EventResponseDto;
import com.example.booking_system.dto.update.UpdateEventDto;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface EventService {
    EventResponseDto createEvent(@Valid EventRequestDto eventRequestDto);
    Page<EventResponseDto> getAllEvents(Pageable pageable);
    EventResponseDto getEvent(UUID eventId);
    Page<EventResponseDto> findEventsByCriteria(@Valid EventFilterDto filter, Pageable pageable);

    Page<EventResponseDto> getMyCreatedEvents(Pageable pageable);

    EventResponseDto updateEvent(UUID id, @Valid UpdateEventDto updateEventDto);

    Page<EventParticipantResponseDto> getEventParticipants(UUID eventId, Pageable pageable);
    EventResponseDto cancelEvent(UUID eventId);
}
