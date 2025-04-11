package com.example.booking_system.mapper;

import com.example.booking_system.dto.response.RegistrationResponseDto;
import com.example.booking_system.entity.Registration;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(
        componentModel = MappingConstants.ComponentModel.SPRING,
        uses = {UserMapper.class, EventMapper.class},
        injectionStrategy = InjectionStrategy.CONSTRUCTOR
)
public interface RegistrationMapper {
    RegistrationResponseDto registrationToRegistrationResponseDto(Registration registration);

}
