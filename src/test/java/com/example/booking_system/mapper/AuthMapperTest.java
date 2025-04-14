package com.example.booking_system.mapper;

import com.example.booking_system.dto.request.RegisterRequestDto;
import com.example.booking_system.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AuthMapperTest {

    private AuthMapper mapper;

    @BeforeEach
    void setUp() {
        mapper = new AuthMapperImpl();
    }

    @Test
    void shouldMapRegisterRequestDtoToUser() {
        RegisterRequestDto registerRequestDto = new RegisterRequestDto(
                "testuser",
                "test.user@example.com",
                "password",
                "Jan",
                "Kowalski"
        );

        User user = mapper.registerRequestDtoToUser(registerRequestDto);

        assertNotNull(user);
        assertEquals(registerRequestDto.username(), user.getUsername());
        assertEquals(registerRequestDto.email(), user.getEmail());
        assertEquals(registerRequestDto.firstName(), user.getFirstName());
        assertEquals(registerRequestDto.lastName(), user.getLastName());
        assertNull(user.getId(), "ID should be ignored by mapper");
        assertNull(user.getPassword(), "Password should be ignored by mapper");
        assertNull(user.getCreatedAt(), "CreatedAt should be ignored by mapper");
        assertNull(user.getUpdatedAt(), "UpdatedAt should be ignored by mapper");
        assertTrue(user.getOrganizedEvents().isEmpty(), "OrganizedEvents should be ignored by mapper");
        assertTrue(user.getRoles().isEmpty(), "Roles should be ignored by mapper");
        assertTrue(user.getRegistrations().isEmpty(), "Registrations should be ignored by mapper");
        assertEquals(true, user.getEnable(), "Enable should be true by default");


    }
}