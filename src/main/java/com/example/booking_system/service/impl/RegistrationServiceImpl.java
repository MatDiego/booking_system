package com.example.booking_system.service.impl;

import com.example.booking_system.dto.response.RegistrationResponseDto;
import com.example.booking_system.entity.Event;
import com.example.booking_system.entity.Registration;
import com.example.booking_system.entity.User;
import com.example.booking_system.entity.enums.RegistrationStatus;
import com.example.booking_system.exception.RegistrationConflictException;
import com.example.booking_system.exception.ResourceNotFoundException;
import com.example.booking_system.mapper.RegistrationMapper;
import com.example.booking_system.repository.EventRepository;
import com.example.booking_system.repository.RegistrationRepository;
import com.example.booking_system.repository.UserRepository;
import com.example.booking_system.service.RegistrationService;
import com.example.booking_system.service.SecurityContextService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RegistrationServiceImpl implements RegistrationService {
    private final EventRepository eventRepository;
    private final UserRepository userRepository;
    private final RegistrationRepository registrationRepository;
    private final RegistrationMapper registrationMapper;
    private SecurityContextService securityContextService;

    @Override
    @Transactional
    public RegistrationResponseDto registerToEvent(UUID eventId) {
        UUID userId = securityContextService.getCurrentUserId();
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new ResourceNotFoundException("Event", "eventId", eventId));
        User user = userRepository.findByIdWithRoles(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "userId", userId));

        boolean alreadyRegistered = registrationRepository.existsByUserIdAndEventId(userId, eventId);
        if (alreadyRegistered) {
            throw new RegistrationConflictException("User is already registered for this event");
        }

        Registration registration = new Registration();
        registration.setEvent(event);
        registration.setUser(user);
        registrationRepository.save(registration);

        return registrationMapper.registrationToRegistrationResponseDto(registration);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<RegistrationResponseDto> getUserRegistrations(Pageable pageable) {
        UUID userId = securityContextService.getCurrentUserId();
        Page<Registration>  registrationPage = registrationRepository.findByUserId(userId, pageable);
        return registrationPage.map(registrationMapper::registrationToRegistrationResponseDto);
    }

    @Override
    @Transactional
    public RegistrationResponseDto cancelRegistrationByUser(UUID registrationId) {

        Registration registration = registrationRepository.findById(registrationId)
                .orElseThrow(() -> new ResourceNotFoundException("Registration", "id", registrationId));

        RegistrationStatus currentStatus = registration.getStatus();
        RegistrationStatus newStatus = RegistrationStatus.CANCELED_BY_USER;

        if (!currentStatus.canTransitionTo(newStatus)) {
            throw new IllegalStateException("Cannot cancel registration from status: " + currentStatus);
        }

        registration.setStatus(newStatus);
        return registrationMapper.registrationToRegistrationResponseDto(registration);
    }
}
