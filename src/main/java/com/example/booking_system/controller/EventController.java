package com.example.booking_system.controller;

import com.example.booking_system.dto.request.EventRequestDto;
import com.example.booking_system.dto.response.EventResponseDto;
import com.example.booking_system.security.UserPrincipal;
import com.example.booking_system.service.EventService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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
            @Valid @RequestBody EventRequestDto eventRequestDto,
            @AuthenticationPrincipal UserPrincipal userPrincipal
            ) {
        UUID organizerId = userPrincipal.getUserId();
        EventResponseDto eventResponseDto = eventService.createEvent(eventRequestDto, organizerId);

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


}
