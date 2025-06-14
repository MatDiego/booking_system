package com.example.booking_system.service.impl;

import com.example.booking_system.dto.filter.EventFilterDto;
import com.example.booking_system.dto.request.EventRequestDto;
import com.example.booking_system.dto.response.EventResponseDto;
import com.example.booking_system.entity.Event;
import com.example.booking_system.entity.User;
import com.example.booking_system.exception.ResourceNotFoundException;
import com.example.booking_system.mapper.EventMapper;
import com.example.booking_system.repository.EventRepository;
import com.example.booking_system.repository.UserRepository;
import com.example.booking_system.repository.specification.EventSpecifications;
import com.example.booking_system.security.UserPrincipal;
import com.example.booking_system.service.EventService;
import com.example.booking_system.service.SecurityContextService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class EventServiceImpl implements EventService {

    private final EventRepository eventRepository;
    private final EventMapper eventMapper;
    private final UserRepository userRepository;
    private final SecurityContextService securityContextService;


    @Override
    @Transactional
    public EventResponseDto createEvent(EventRequestDto eventRequestDto) {

        UUID organizerId = securityContextService.getCurrentUserId();
        User organizer = userRepository.findByIdWithRoles(organizerId)
                .orElseThrow();

        Event event = eventMapper.eventRequestDtoToEvent(eventRequestDto);
        event.setOrganizer(organizer);
        Event savedEvent = eventRepository.save(event);

        return eventMapper.eventToEventResponseDto(savedEvent);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<EventResponseDto> getAllEvents(Pageable pageable) {
        Page<Event> eventPage = eventRepository.findAll(pageable);
        return eventPage.map(eventMapper::eventToEventResponseDto);
    }

    @Override
    @Transactional(readOnly = true)
    public EventResponseDto getEvent(UUID eventId) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new ResourceNotFoundException("Event", "eventId", eventId));
        return eventMapper.eventToEventResponseDto(event);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<EventResponseDto> findEventsByCriteria(
            EventFilterDto filter,
            Pageable pageable) {

        Specification<Event> combinedSpec = Specification.where(null);

        if (filter.eventType() != null) {
            combinedSpec = combinedSpec.and(EventSpecifications.hasEventType(filter.eventType()));
        }

        if (filter.location() != null && !filter.location().isBlank()) {
            combinedSpec = combinedSpec.and(EventSpecifications.hasLocationLike(filter.location()));
        }

        if (filter.dateFrom() != null) {
            combinedSpec = combinedSpec.and(EventSpecifications.isAfterDate(filter.dateFrom()));
        }

        if (filter.dateTo() != null) {
            combinedSpec = combinedSpec.and(EventSpecifications.isBeforeDate(filter.dateTo()));
        }

        if (filter.priceMin() != null && filter.priceMax() != null) {
            combinedSpec = combinedSpec.and(EventSpecifications.hasPriceBetween(filter.priceMin(), filter.priceMax()));
        }

        if (filter.organizerId() != null) {
            combinedSpec = combinedSpec.and(EventSpecifications.hasOrganizer(filter.organizerId()));
        }

        if (filter.title() != null) {
            combinedSpec = combinedSpec.and(EventSpecifications.titleContains(filter.title()));
        }

        Page<Event> eventPage = eventRepository.findAll(combinedSpec, pageable);
        return eventPage.map(eventMapper::eventToEventResponseDto);
    }
}
