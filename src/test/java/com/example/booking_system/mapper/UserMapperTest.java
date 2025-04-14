package com.example.booking_system.mapper;

import com.example.booking_system.dto.response.UserResponseDto;
import com.example.booking_system.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserMapperTest {

    private UserMapper userMapper;

    @BeforeEach
    void setUp() {
        userMapper = new UserMapperImpl();
    }

    @Test
    void shouldMapUserToNull() {
        UserResponseDto userResponseDto = userMapper.userToUserResponse(null);
        assertNull(userResponseDto);
    }

    @Test
    void shouldMapUserToUserResponse() {
        User user = new User();
        user.setEmail("test.user@example.com");
        user.setFirstName("Jan");
        user.setLastName("Kowalski");
        user.setUsername("testuser");

        UserResponseDto userResponseDto = userMapper.userToUserResponse(user);
        assertNotNull(userResponseDto);
        assertEquals(user.getFirstName(), userResponseDto.firstName());
        assertEquals(user.getLastName(), userResponseDto.lastName());
        assertEquals(user.getUsername(), userResponseDto.username());
        assertEquals(user.getEmail(), userResponseDto.email());
    }
}