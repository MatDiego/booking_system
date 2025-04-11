package com.example.booking_system.service.impl;

import com.example.booking_system.dto.response.RegistrationResponseDto;
import com.example.booking_system.entity.Event;
import com.example.booking_system.entity.Registration;
import com.example.booking_system.entity.User;
import com.example.booking_system.entity.enums.StatusType;
import com.example.booking_system.exception.RegistrationConflictException;
import com.example.booking_system.exception.ResourceNotFoundException;
import com.example.booking_system.mapper.RegistrationMapper;
import com.example.booking_system.repository.EventRepository;
import com.example.booking_system.repository.RegistrationRepository;
import com.example.booking_system.repository.UserRepository;
import com.example.booking_system.service.RegistrationService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RegistrationServiceImpl implements RegistrationService {
    private final EventRepository eventRepository;
    private final UserRepository userRepository;
    private final RegistrationRepository registrationRepository;
    private final RegistrationMapper registrationMapper;
    @Override
    @Transactional
    public RegistrationResponseDto registerToEvent(UUID userId, UUID eventId) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new ResourceNotFoundException("Event", "eventId", eventId));
        User user = userRepository.findByIdWithRoles(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "userId", userId));

        boolean alreadyRegistered = registrationRepository.findByUserIdAndEventId(userId, eventId);
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
    @Transactional
    public Page<RegistrationResponseDto> getUserRegistrations(UUID userId, Pageable pageable) {
        Page<Registration>  registrationPage = registrationRepository.findByUserId(userId, pageable);
        return registrationPage.map(registrationMapper::registrationToRegistrationResponseDto);
    }
}
