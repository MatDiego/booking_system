package com.example.booking_system.service.impl;

import com.example.booking_system.dto.filter.EventFilterDto;
import com.example.booking_system.dto.request.EventRequestDto;
import com.example.booking_system.dto.response.EventParticipantResponseDto;
import com.example.booking_system.dto.response.EventResponseDto;
import com.example.booking_system.dto.update.UpdateEventDto;
import com.example.booking_system.entity.Event;
import com.example.booking_system.entity.Registration;
import com.example.booking_system.entity.User;
import com.example.booking_system.entity.enums.EventStatus;
import com.example.booking_system.entity.enums.RegistrationStatus;
import com.example.booking_system.exception.ResourceNotFoundException;
import com.example.booking_system.mapper.EventMapper;
import com.example.booking_system.mapper.RegistrationMapper;
import com.example.booking_system.repository.EventRepository;
import com.example.booking_system.repository.RegistrationRepository;
import com.example.booking_system.repository.UserRepository;
import com.example.booking_system.repository.specification.EventSpecifications;
import com.example.booking_system.service.EventService;
import com.example.booking_system.service.security.SecurityContextService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.util.UUID;

@Service
@Validated
@RequiredArgsConstructor
public class EventServiceImpl implements EventService {

    private final EventRepository eventRepository;
    private final EventMapper eventMapper;
    private final UserRepository userRepository;
    private final SecurityContextService securityContextService;
    private final RegistrationRepository registrationRepository;
    private final RegistrationMapper registrationMapper;

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

        EventStatus statusToFilter;

        if (filter.eventStatus() == null) {
            statusToFilter = EventStatus.SCHEDULED;
        } else {
            statusToFilter = filter.eventStatus();
        }

        combinedSpec = combinedSpec.and(EventSpecifications.hasEventStatus(statusToFilter));
        Page<Event> eventPage = eventRepository.findAll(combinedSpec, pageable);
        return eventPage.map(eventMapper::eventToEventResponseDto);
    }

    @Override
    public Page<EventResponseDto> getMyCreatedEvents(Pageable pageable) {
        UUID organizerId = securityContextService.getCurrentUserId();
        Page<Event> organizerEvents = eventRepository.findByOrganizerId(organizerId, pageable);

        return organizerEvents.map(eventMapper::eventToEventResponseDto);
    }

    @Override
    @Transactional
    public EventResponseDto updateEvent(UUID eventId, UpdateEventDto dto) {
        Event eventToUpdate = eventRepository.findById(eventId)
                .orElseThrow(() -> new ResourceNotFoundException("Event", "id", eventId));

        if (dto.title() != null && !dto.title().isBlank()) {
            eventToUpdate.setTitle(dto.title());
        }
        if (dto.description() != null) {
            eventToUpdate.setDescription(dto.description());
        }
        if (dto.startDate() != null) {
            eventToUpdate.setStartDate(dto.startDate());
        }
        if (dto.endDate() != null) {
            eventToUpdate.setEndDate(dto.endDate());
        }
        if (dto.location() != null) {
            eventToUpdate.setLocation(dto.location());
        }
        if (dto.capacity() != null) {
            eventToUpdate.setCapacity(dto.capacity());
        }
        if (dto.eventType() != null) {
            eventToUpdate.setEventType(dto.eventType());
        }
        if (dto.price() != null) {
            eventToUpdate.setPrice(dto.price());
        }
        if (dto.eventStatus() != null) {
            eventToUpdate.setEventStatus(dto.eventStatus());
        }

        return eventMapper.eventToEventResponseDto(eventToUpdate);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<EventParticipantResponseDto> getEventParticipants(UUID eventId, Pageable pageable) {
        if (!eventRepository.existsById(eventId)) {
            throw new ResourceNotFoundException("Event", "id", eventId);
        }
        Page<Registration> registrations = registrationRepository.findByEventIdWithUser(eventId, pageable);
        return registrations.map(registrationMapper::registrationToParticipantResponseDto);
    }

    @Override
    @Transactional
    public EventResponseDto cancelEvent(UUID eventId) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new ResourceNotFoundException("Event", "id", eventId));

        if (!event.getEventStatus().canTransitionTo(EventStatus.CANCELED)) {
            throw new IllegalStateException("Cannot cancel event from status: " + event.getEventStatus());
        }

        event.setEventStatus(EventStatus.CANCELED);
        registrationRepository.updateStatusByEventId(eventId, RegistrationStatus.CANCELED_BY_EVENT);

        return eventMapper.eventToEventResponseDto(event);
    }
}
