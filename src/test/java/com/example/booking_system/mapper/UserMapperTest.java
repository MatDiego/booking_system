package com.example.booking_system.mapper;

import com.example.booking_system.dto.response.UserResponseDto;
import com.example.booking_system.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;


class UserMapperTest {

    private UserMapper userMapper;

    @BeforeEach
    void setUp() {
        userMapper = new UserMapperImpl();
    }

    @Test
    void returns_null_when_user_entity_is_null() {
        UserResponseDto userResponseDto = userMapper.userToUserResponse(null);
        assertThat(userResponseDto).isNull();
    }

    @Test
    void maps_user_entity_to_user_response_dto() {
        User user = new User();
        user.setEmail("test.user@example.com");
        user.setFirstName("Jan");
        user.setLastName("Kowalski");
        user.setUsername("testuser");

        UserResponseDto userResponseDto = userMapper.userToUserResponse(user);

        assertThat(userResponseDto).isNotNull();
        assertThat(userResponseDto.firstName()).isEqualTo(user.getFirstName());
        assertThat(userResponseDto.lastName()).isEqualTo(user.getLastName());
        assertThat(userResponseDto.username()).isEqualTo(user.getUsername());
        assertThat(userResponseDto.email()).isEqualTo(user.getEmail());
    }
}