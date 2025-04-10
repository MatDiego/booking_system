package com.example.booking_system.mapper;

import com.example.booking_system.entity.User;
import com.example.booking_system.dto.request.RegisterRequestDto;
import org.mapstruct.*;
import org.springframework.stereotype.Component;

@Mapper(
        componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        injectionStrategy = InjectionStrategy.CONSTRUCTOR
)
@Component
public interface AuthMapper {
    @Mapping(target = "id", ignore = true) // ID ustawimy w serwisie (np. UUID) lub baza ustawi
    @Mapping(target = "password", ignore = true) // Hasło zahashujemy w serwisie
    @Mapping(target = "roles", ignore = true) // Role przypiszemy w serwisie
    @Mapping(target = "enable", ignore = true) // Ustawimy w serwisie
    @Mapping(target = "createdAt", ignore = true) // Zarządzane przez JPA/Hibernate
    @Mapping(target = "updatedAt", ignore = true) // Zarządzane przez JPA/Hibernate
    @Mapping(target = "registrations", ignore = true)
    User registerRequestDtoToUser(RegisterRequestDto registerRequest);
}
