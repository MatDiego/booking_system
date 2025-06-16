package com.example.booking_system.mapper;

import com.example.booking_system.dto.response.EventParticipantResponseDto;
import com.example.booking_system.dto.response.RegistrationResponseDto;
import com.example.booking_system.entity.Registration;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(
        componentModel = MappingConstants.ComponentModel.SPRING,
        uses = {UserMapper.class, EventMapper.class},
        injectionStrategy = InjectionStrategy.CONSTRUCTOR
)
public interface RegistrationMapper {
    RegistrationResponseDto registrationToRegistrationResponseDto(Registration registration);

    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "user", target = ".")
    @Mapping(source = "status", target = "registrationStatus")
    EventParticipantResponseDto registrationToParticipantResponseDto(Registration registration);
}
