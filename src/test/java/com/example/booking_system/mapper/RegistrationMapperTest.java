package com.example.booking_system.mapper;

import com.example.booking_system.dto.response.EventResponseDto;
import com.example.booking_system.dto.response.RegistrationResponseDto;
import com.example.booking_system.dto.response.UserResponseDto;
import com.example.booking_system.entity.Event;
import com.example.booking_system.entity.Registration;
import com.example.booking_system.entity.User;
import com.example.booking_system.entity.enums.EventType;
import com.example.booking_system.entity.enums.StatusType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

class RegistrationMapperTest {

    private RegistrationMapper registrationMapper;
    private UserMapper userMapper;
    private EventMapper eventMapper;

    @BeforeEach
    void setUp() {
        userMapper = mock(UserMapper.class);
        eventMapper = mock(EventMapper.class);
        registrationMapper = new RegistrationMapperImpl(userMapper,eventMapper);
    }

    @Test
    void shouldMapRegistrationToRegistrationResponseDto() {
        Registration registration = new Registration();
        UUID uuid = UUID.randomUUID();
        LocalDateTime registrationDate = LocalDateTime.now();

        User user = new User();
        user.setEmail("test.user@example.com");
        user.setFirstName("Jan");
        user.setLastName("Kowalski");
        user.setUsername("testuser");

        Event event = new Event();
        LocalDateTime startDate = LocalDateTime.now().plusDays(1);
        LocalDateTime endDate = LocalDateTime.now().plusDays(1).plusHours(2);
        BigDecimal price = new BigDecimal("99.99");
        int capacity = 300;

        event.setTitle("Test Event Title");
        event.setDescription("Test Event Description");
        event.setStartDate(startDate);
        event.setEndDate(endDate);
        event.setLocation("Test Location");
        event.setCapacity(capacity);
        event.setEventType(EventType.CONFERENCE);
        event.setPrice(price);
        event.setOrganizer(user);

        registration.setId(uuid);
        registration.setUser(user);
        registration.setEvent(event);
        registration.setRegistrationDate(registrationDate);
        registration.setStatus(StatusType.PENDING);

        UserResponseDto expectedUserResponseDto = new UserResponseDto(
                user.getId(),
                user.getFirstName(),
                user.getLastName(),
                user.getUsername(),
                user.getEmail()
        );


        EventResponseDto expectedEventResponseDto = new EventResponseDto(
                event.getId(),
                event.getTitle(),
                event.getDescription(),
                event.getStartDate(),
                event.getEndDate(),
                event.getLocation(),
                event.getCapacity(),
                event.getEventType(),
                event.getPrice(),
                expectedUserResponseDto
        );

        when(userMapper.userToUserResponse(user)).thenReturn(expectedUserResponseDto);
        when(eventMapper.eventToEventResponseDto(event)).thenReturn(expectedEventResponseDto);

        RegistrationResponseDto registrationResponseDto = registrationMapper.
                registrationToRegistrationResponseDto(registration);

        assertNotNull(registrationResponseDto);
        assertNotNull(registrationResponseDto.user());
        assertNotNull(registrationResponseDto.event());
        assertEquals(uuid, registrationResponseDto.id());
        assertEquals(expectedUserResponseDto, registrationResponseDto.user());
        assertEquals(expectedEventResponseDto, registrationResponseDto.event());
        assertEquals(registrationDate, registrationResponseDto.registrationDate());
        assertEquals(StatusType.PENDING, registrationResponseDto.status());

        verify(userMapper).userToUserResponse(user);
        verify(eventMapper).eventToEventResponseDto(event);
        verifyNoMoreInteractions(userMapper, eventMapper);
    }
}