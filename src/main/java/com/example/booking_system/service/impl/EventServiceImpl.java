package com.example.booking_system.service.impl;

import com.example.booking_system.dto.request.EventRequestDto;
import com.example.booking_system.dto.response.EventResponseDto;
import com.example.booking_system.entity.Event;
import com.example.booking_system.entity.User;
import com.example.booking_system.mapper.EventMapper;
import com.example.booking_system.repository.EventRepository;
import com.example.booking_system.repository.UserRepository;
import com.example.booking_system.service.EventService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class EventServiceImpl implements EventService {

    private final EventRepository eventRepository;
    private final EventMapper eventMapper;
    private final UserRepository userRepository;


    @Override
    @Transactional
    public EventResponseDto createEvent(EventRequestDto eventRequestDto, String organizerUsername) {
        User organizer = userRepository.findByUsernameWithRoles(organizerUsername)
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
}
