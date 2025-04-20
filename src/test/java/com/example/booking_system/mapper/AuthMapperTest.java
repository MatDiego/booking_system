package com.example.booking_system.mapper;

import com.example.booking_system.dto.request.RegisterRequestDto;
import com.example.booking_system.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class AuthMapperTest {

    private AuthMapper mapper;

    @BeforeEach
    void setUp() {
        mapper = new AuthMapperImpl();
    }

    @Test
    void maps_register_dto_to_user_entity_setting_basic_info_and_default_enabled_status_ignoring_generated_fields() {
        RegisterRequestDto registerRequestDto = new RegisterRequestDto(
                "testuser",
                "test.user@example.com",
                "password",
                "Jan",
                "Kowalski"
        );

        User user = mapper.registerRequestDtoToUser(registerRequestDto);

        assertThat(user).isNotNull();
        assertThat(user.getUsername()).isEqualTo(registerRequestDto.username());
        assertThat(user.getEmail()).isEqualTo(registerRequestDto.email());
        assertThat(user.getFirstName()).isEqualTo(registerRequestDto.firstName());
        assertThat(user.getLastName()).isEqualTo(registerRequestDto.lastName());
        assertThat(user.getId()).as("ID should be ignored by mapper").isNull();
        assertThat(user.getPassword()).as("Password should be ignored by mapper").isNull();
        assertThat(user.getCreatedAt()).as("Created at should be ignored by mapper").isNull();
        assertThat(user.getUpdatedAt()).as("Updated at should be ignored by mapper").isNull();
        assertThat(user.getOrganizedEvents()).as("Organized events should be ignored by mapper").isEmpty();
        assertThat(user.getRoles()).as("Roles should be ignored by mapper").isEmpty();
        assertThat(user.getRegistrations()).as("Registrations should be ignored by mapper").isEmpty();
        assertThat(user.getEnable()).as("Enable should be true by default").isTrue();
    }
}