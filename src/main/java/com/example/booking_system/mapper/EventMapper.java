package com.example.booking_system.mapper;

import com.example.booking_system.dto.request.EventRequestDto;
import com.example.booking_system.dto.response.EventResponseDto;
import com.example.booking_system.dto.update.UpdateEventDto;
import com.example.booking_system.entity.Event;
import org.mapstruct.*;

@Mapper(
        componentModel = MappingConstants.ComponentModel.SPRING,
        uses = UserMapper.class,
        injectionStrategy = InjectionStrategy.CONSTRUCTOR
)
public interface EventMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "organizer", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "registrations", ignore = true)
    Event eventRequestDtoToEvent(EventRequestDto eventRequestDto);

    @Mapping(source = "organizer", target = "userResponseDto")
    EventResponseDto eventToEventResponseDto(Event savedEvent);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "organizer", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "registrations", ignore = true)
    void updateEventFromDto(UpdateEventDto dto, @MappingTarget Event event);
}
