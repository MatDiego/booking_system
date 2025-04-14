package com.example.booking_system.controller;

import com.example.booking_system.dto.response.RegistrationResponseDto;
import com.example.booking_system.security.UserPrincipal;
import com.example.booking_system.service.RegistrationService;
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

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/registrations")
public class RegistrationController {
    private final RegistrationService registrationService;
    @PostMapping("/events/{eventId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<RegistrationResponseDto> registerToEvent(
            @PathVariable UUID eventId,
            @AuthenticationPrincipal UserPrincipal userPrincipal
            ) {
        UUID userId = userPrincipal.getUserId();
        RegistrationResponseDto registrationResponseDto = registrationService.registerToEvent(userId, eventId);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest().path("/api/registrations/{id}")
                .buildAndExpand(registrationResponseDto.id()).toUri();
        return ResponseEntity.created(location).body(registrationResponseDto);
    }

    @GetMapping("/")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Page<RegistrationResponseDto>> getAllRegistrations(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @PageableDefault(sort = "registrationDate")
            Pageable pageable
    ) {
        UUID userId = userPrincipal.getUserId();
        Page<RegistrationResponseDto> registrationDtoPage = registrationService.getUserRegistrations(userId, pageable);
        return ResponseEntity.ok(registrationDtoPage);
    }

}
