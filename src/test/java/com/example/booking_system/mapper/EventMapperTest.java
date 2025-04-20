package com.example.booking_system.mapper;

import com.example.booking_system.dto.request.EventRequestDto;
import com.example.booking_system.dto.response.EventResponseDto;
import com.example.booking_system.dto.response.UserResponseDto;
import com.example.booking_system.entity.Event;
import com.example.booking_system.entity.User;
import com.example.booking_system.entity.enums.EventType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;


import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;


class EventMapperTest {


    private EventMapper mapper;
    @Mock
    private UserMapper userMapper;
    private LocalDateTime testStartDate;
    private LocalDateTime testEndDate;
    private BigDecimal testPrice;
    private Integer testCapacity;
    private User testOrganizer;
    private UserResponseDto testOrganizerDto;


    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mapper = new EventMapperImpl(userMapper);

        testStartDate = LocalDateTime.now().plusDays(1);
        testEndDate = testStartDate.plusHours(5);
        testPrice = new BigDecimal("149.50");
        testCapacity = 250;

        testOrganizer = new User();
        testOrganizer.setId(UUID.randomUUID());
        testOrganizer.setFirstName("Anna");
        testOrganizer.setLastName("Nowak");
        testOrganizer.setEmail("a.nowak@example.com");
        testOrganizer.setUsername("annanowak");

        testOrganizerDto = new UserResponseDto(
                testOrganizer.getId(),
                testOrganizer.getFirstName(),
                testOrganizer.getLastName(),
                testOrganizer.getUsername(),
                testOrganizer.getEmail()
        );
    }

    @Test
    void maps_event_request_dto_to_event_entity_ignoring_generated_and_relational_fields() {

        EventRequestDto eventRequestDto = new EventRequestDto(
                "Konferencja Java",
                "Wszystko o nowościach w Javie",
                testStartDate,
                testEndDate,
                "Kraków",
                testCapacity,
                EventType.CONFERENCE,
                testPrice
        );
        Event event = mapper.eventRequestDtoToEvent(eventRequestDto);

        assertThat(event).isNotNull();
        assertThat(event.getTitle()).isEqualTo(eventRequestDto.title());
        assertThat(event.getDescription()).isEqualTo(eventRequestDto.description());
        assertThat(event.getStartDate()).isEqualTo(testStartDate);
        assertThat(event.getEndDate()).isEqualTo(testEndDate);
        assertThat(event.getCapacity()).isEqualTo(testCapacity);
        assertThat(event.getLocation()).isEqualTo(eventRequestDto.location());
        assertThat(event.getPrice()).isEqualTo(testPrice);
        assertThat(event.getEventType()).isEqualTo(eventRequestDto.eventType());
        assertThat(event.getId()).as("ID should be ignored by mapper").isNull();
        assertThat(event.getOrganizer()).as("Organizer should be ignored by mapper").isNull();
        assertThat(event.getCreatedAt()).as("Created at should be ignored by mapper").isNull();
        assertThat(event.getUpdatedAt()).as("Updated at should be ignored by mapper").isNull();
        assertThat(event.getRegistrations())
                .as("Registrations should be ignored by mapper").isEmpty();
    }

    @Test
    void maps_event_entity_to_event_response_dto_using_user_mapper_for_organizer() {
        Event event = new Event();
        event.setId(UUID.randomUUID());
        event.setTitle("Warsztaty AssertJ");
        event.setDescription("Naucz się pisać lepsze asercje");
        event.setStartDate(testStartDate);
        event.setEndDate(testEndDate);
        event.setLocation("Online");
        event.setCapacity(testCapacity);
        event.setEventType(EventType.WORKSHOP);
        event.setPrice(testPrice);
        event.setOrganizer(testOrganizer);

        when(userMapper.userToUserResponse(testOrganizer)).thenReturn(testOrganizerDto);

        EventResponseDto eventResponseDto = mapper.eventToEventResponseDto(event);

        assertThat(eventResponseDto).isNotNull();
        assertThat(eventResponseDto.userResponseDto()).isNotNull();
        assertThat(eventResponseDto.title()).isEqualTo(event.getTitle());
        assertThat(eventResponseDto.description()).isEqualTo(event.getDescription());
        assertThat(eventResponseDto.startDate()).isEqualTo(event.getStartDate());
        assertThat(eventResponseDto.endDate()).isEqualTo(event.getEndDate());
        assertThat(eventResponseDto.capacity()).isEqualTo(event.getCapacity());
        assertThat(eventResponseDto.location()).isEqualTo(event.getLocation());
        assertThat(eventResponseDto.eventType()).isEqualTo(event.getEventType());
        assertThat(eventResponseDto.price()).isEqualTo(event.getPrice());
        assertThat(eventResponseDto.userResponseDto())
                .as("UserResponseDto should be populated by UserMapper")
                .isNotNull()
                .isEqualTo(testOrganizerDto);

        verify(userMapper).userToUserResponse(testOrganizer);
        verifyNoMoreInteractions(userMapper);
    }
}