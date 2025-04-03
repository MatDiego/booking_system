package com.example.booking_system.mapper;

import com.example.booking_system.entity.User;
import com.example.booking_system.dto.request.RegisterRequestDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import org.springframework.stereotype.Component;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
@Component
public interface AuthMapper {
    @Mapping(target = "id", ignore = true) // ID ustawimy w serwisie (np. UUID) lub baza ustawi
    @Mapping(target = "password", ignore = true) // Hasło zahashujemy w serwisie
    @Mapping(target = "roles", ignore = true) // Role przypiszemy w serwisie
    @Mapping(target = "enabled", ignore = true) // Ustawimy w serwisie
    @Mapping(target = "createdAt", ignore = true) // Zarządzane przez JPA/Hibernate
    @Mapping(target = "updatedAt", ignore = true) // Zarządzane przez JPA/Hibernate
    User registerRequestDtoToUser(RegisterRequestDto registerRequest);
}
