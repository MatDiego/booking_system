package com.example.booking_system.controller;

import com.example.booking_system.dto.filter.EventFilterDto;
import com.example.booking_system.dto.request.EventRequestDto;
import com.example.booking_system.dto.response.EventParticipantResponseDto;
import com.example.booking_system.dto.response.EventResponseDto;
import com.example.booking_system.dto.update.UpdateEventDto;
import com.example.booking_system.service.EventService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.UUID;

@RestController
@RequestMapping("/api/events")
@RequiredArgsConstructor
public class EventController {

    private final EventService eventService;

    @PostMapping
    @PreAuthorize("hasRole('ORGANIZER') or hasRole('ADMIN')")
    public ResponseEntity<EventResponseDto> createEvent(
            @Valid @RequestBody EventRequestDto eventRequestDto
            ) {
        EventResponseDto eventResponseDto = eventService.createEvent(eventRequestDto);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest().path("/{id}")
                .buildAndExpand(eventResponseDto.id()).toUri();

        return ResponseEntity.created(location).body(eventResponseDto);
    }

    @GetMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Page<EventResponseDto>> getAllEvents(
            @PageableDefault(size = 10, sort = "startDate")
            Pageable pageable
    ) {
        Page<EventResponseDto> eventPage = eventService.getAllEvents(pageable);
        return ResponseEntity.ok(eventPage);
    }

    @GetMapping("/{eventId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<EventResponseDto> getEvent(
            @PathVariable UUID eventId
            ) {
       return ResponseEntity.ok(eventService.getEvent(eventId));
    }

    @GetMapping("/search")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Page<EventResponseDto>> getFilteredEvents(
            @Validated EventFilterDto filter,
            Pageable pageable) {
        Page<EventResponseDto> eventPage = eventService.findEventsByCriteria(filter, pageable);
        return ResponseEntity.ok(eventPage);
    }

    @GetMapping("my-events")
    @PreAuthorize("hasRole('ORGANIZER') or hasRole('ADMIN')")
    public ResponseEntity<Page<EventResponseDto>> getMyEvents(
            @PageableDefault(sort = "startDate")
            Pageable pageable
    ) {
        Page<EventResponseDto> eventResponseDto = eventService.getMyCreatedEvents(pageable);
        return ResponseEntity.ok(eventResponseDto);
    }

    @PatchMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or (hasRole('ORGANIZER') and @eventSecurityService.isEventOwner(#id))")
    public ResponseEntity<EventResponseDto> updateEvent(
            @PathVariable UUID id,
            @Valid @RequestBody UpdateEventDto updateEventDto) {
        EventResponseDto updatedEvent = eventService.updateEvent(id, updateEventDto);
        return ResponseEntity.ok(updatedEvent);
    }

    @GetMapping("/{id}/participants")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Page<EventParticipantResponseDto>> getParticipants(
            @PathVariable UUID id,
            @PageableDefault(sort = "registrationDate")
            Pageable pageable
    ) {
        Page<EventParticipantResponseDto> eventResponseDto = eventService.getEventParticipants(id, pageable);
        return ResponseEntity.ok(eventResponseDto);
    }

    @PutMapping("/{id}/cancel")
    @PreAuthorize("hasRole('ADMIN') or @eventSecurityService.isEventOwner(#id)")
    public ResponseEntity<EventResponseDto> cancelEvent(@PathVariable UUID id) {
        EventResponseDto canceledEvent = eventService.cancelEvent(id);
        return ResponseEntity.ok(canceledEvent);
    }
}
