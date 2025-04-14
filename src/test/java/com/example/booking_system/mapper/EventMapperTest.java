package com.example.booking_system.mapper;

import com.example.booking_system.dto.request.EventRequestDto;
import com.example.booking_system.dto.response.EventResponseDto;
import com.example.booking_system.dto.response.UserResponseDto;
import com.example.booking_system.entity.Event;
import com.example.booking_system.entity.User;
import com.example.booking_system.entity.enums.EventType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


class EventMapperTest {


    private EventMapper mapper;
    private UserMapper userMapper;


    @BeforeEach
    void setUp() {
        userMapper = mock(UserMapper.class);
        mapper = new EventMapperImpl(userMapper);
    }

    @Test
    void shouldMapEventRequestDtoToEvent() {

        LocalDateTime startDate = LocalDateTime.now().minusDays(1);
        LocalDateTime endDate = LocalDateTime.now();
        BigDecimal price = BigDecimal.valueOf(100);
        Integer capacity = 300;

        EventRequestDto eventRequestDto = new EventRequestDto(
                "Test title",
                "Test description",
                startDate,
                endDate,
                "Warsaw",
                capacity,
                EventType.CONFERENCE,
                price
        );
        Event event = mapper.eventRequestDtoToEvent(eventRequestDto);

        assertNotNull(event);
        assertEquals(eventRequestDto.title(), event.getTitle());
        assertEquals(eventRequestDto.description(), event.getDescription());
        assertEquals(startDate, event.getStartDate());
        assertEquals(endDate, event.getEndDate());
        assertEquals(eventRequestDto.location(), event.getLocation());
        assertEquals(capacity, event.getCapacity());
        assertEquals(price, event.getPrice());
        assertEquals(eventRequestDto.eventType(), event.getEventType());
        assertNull(event.getId(), "ID should be ignored by mapper");
        assertNull(event.getOrganizer(), "Organizer should be ignored by mapper");
        assertNull(event.getCreatedAt(), "Created at should be ignored by mapper");
        assertNull(event.getUpdatedAt(), "Updated at should be ignored by mapper");
        assertTrue(event.getRegistrations().isEmpty(), "Registrations should be ignored by mapper");


    }

    @Test
    void shouldMapEventToEventResponseDto() {
        User organizer = new User();
        organizer.setFirstName("Jan");
        organizer.setLastName("Kowalski");
        organizer.setEmail("j@k.com");
        organizer.setUsername("jk");


        Event event = new Event();
        LocalDateTime startDate = LocalDateTime.now().plusDays(1);
        LocalDateTime endDate = LocalDateTime.now().plusDays(1).plusHours(2);
        BigDecimal price = new BigDecimal("99.99");
        int capacity = 100;

        event.setTitle("Test Event Title");
        event.setDescription("Test Event Description");
        event.setStartDate(startDate);
        event.setEndDate(endDate);
        event.setLocation("Test Location");
        event.setCapacity(capacity);
        event.setEventType(EventType.CONFERENCE);
        event.setPrice(price);
        event.setOrganizer(organizer);

        UserResponseDto expectedUserResponseDto = new UserResponseDto(
                organizer.getId(),
                organizer.getFirstName(),
                organizer.getLastName(),
                organizer.getUsername(),
                organizer.getEmail()
        );

        when(userMapper.userToUserResponse(organizer)).thenReturn(expectedUserResponseDto);
        EventResponseDto eventResponseDto = mapper.eventToEventResponseDto(event);

        assertNotNull(eventResponseDto);
        assertNotNull(eventResponseDto.userResponseDto());
        assertEquals(event.getTitle(), eventResponseDto.title());
        assertEquals(event.getDescription(), eventResponseDto.description());
        assertEquals(event.getStartDate(), eventResponseDto.startDate());
        assertEquals(event.getEndDate(), eventResponseDto.endDate());
        assertEquals(event.getLocation(), eventResponseDto.location());
        assertEquals(event.getCapacity(), eventResponseDto.capacity());
        assertEquals(event.getEventType(), eventResponseDto.eventType());
        assertEquals(event.getPrice(), eventResponseDto.price());
        assertEquals(expectedUserResponseDto, eventResponseDto.userResponseDto());

        verify(userMapper).userToUserResponse(organizer);
        verifyNoMoreInteractions(userMapper);
    }
}