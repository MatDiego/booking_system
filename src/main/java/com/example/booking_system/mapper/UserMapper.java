package com.example.booking_system.mapper;

import com.example.booking_system.dto.request.UserRequestDto;
import com.example.booking_system.dto.response.UserResponseDto;
import com.example.booking_system.entity.Role;
import com.example.booking_system.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
@Component
public interface UserMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "password", ignore = true)
    @Mapping(target = "roles", ignore = true)
    @Mapping(target = "organizedEvents", ignore = true)
    @Mapping(target = "registrations", ignore = true)
    @Mapping(target = "enable", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    User userRegistrationRequestToUser(UserRequestDto userRequestDto);

    @Mapping(target = "roles", source = "roles", qualifiedByName = "rolesToRoleNames")
    UserResponseDto userToUserResponse(User user);
    @Named("rolesToRoleNames")
    default Set<String> mapRolesToStrings(Set<Role> roles) {
        if (roles == null) {
            return Collections.emptySet();
        }
        return roles.stream()
                .map(role -> role.getName().name())
                .collect(Collectors.toSet());
    }

}
