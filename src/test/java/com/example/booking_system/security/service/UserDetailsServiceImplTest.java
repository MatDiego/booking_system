package com.example.booking_system.security.service;

import com.example.booking_system.entity.User;
import com.example.booking_system.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

class UserDetailsServiceImplTest {

    private UserRepository userRepository;
    private UserDetailsServiceImpl userDetailsServiceImpl;

    @BeforeEach
    void setUp() {
        userRepository = Mockito.mock(UserRepository.class);
        userDetailsServiceImpl = new UserDetailsServiceImpl(userRepository);
    }

    @Test
    void shouldLoadUserByUsername() {
        User user = new User();
        user.setId(UUID.randomUUID());
        user.setUsername("testuser");
        user.setPassword("password");


        when(userRepository.findByUsernameWithRoles(user.getUsername())).thenReturn(Optional.of(user));
        UserDetails actualUser = userDetailsServiceImpl.loadUserByUsername(user.getUsername());

        assertNotNull(actualUser);
        assertEquals(user.getUsername(), actualUser.getUsername());
        assertEquals(user.getPassword(), actualUser.getPassword());

    }
}