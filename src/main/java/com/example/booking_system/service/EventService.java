package com.example.booking_system.service;

import com.example.booking_system.dto.request.EventRequestDto;
import com.example.booking_system.dto.response.EventResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface EventService {
    EventResponseDto createEvent(EventRequestDto eventRequestDto, String organizerUsername);
    Page<EventResponseDto> getAllEvents(Pageable pageable);
}
