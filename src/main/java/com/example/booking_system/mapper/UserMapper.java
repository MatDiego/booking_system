package com.example.booking_system.mapper;

import com.example.booking_system.dto.response.UserResponseDto;
import com.example.booking_system.entity.User;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(
        componentModel = MappingConstants.ComponentModel.SPRING,
        injectionStrategy = InjectionStrategy.CONSTRUCTOR
)
public interface UserMapper {
    UserResponseDto userToUserResponse(User user);
}
