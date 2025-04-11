package com.example.booking_system.mapper;

import com.example.booking_system.dto.request.RegisterRequestDto;
import com.example.booking_system.entity.User;
import org.mapstruct.*;

@Mapper(
        componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        injectionStrategy = InjectionStrategy.CONSTRUCTOR
)
public interface AuthMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "password", ignore = true)
    @Mapping(target = "roles", ignore = true)
    @Mapping(target = "enable", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "registrations", ignore = true)
    User registerRequestDtoToUser(RegisterRequestDto registerRequest);
}
