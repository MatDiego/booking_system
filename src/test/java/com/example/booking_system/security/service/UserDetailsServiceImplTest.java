package com.example.booking_system.security.service;

import com.example.booking_system.entity.User;
import com.example.booking_system.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.Mockito.when;

class UserDetailsServiceImplTest {

    private UserRepository userRepository;
    private UserDetailsServiceImpl userDetailsServiceImpl;
    private User testUser;

    @BeforeEach
    void setUp() {
        userRepository = Mockito.mock(UserRepository.class);
        userDetailsServiceImpl = new UserDetailsServiceImpl(userRepository);
        testUser = new User();
        testUser.setId(UUID.randomUUID());
        testUser.setUsername("testuser");
        testUser.setPassword("password");
    }

    @Test
    void loads_user_details_when_user_exists_in_repository() {
        when(userRepository.findByUsernameWithRoles(testUser.getUsername())).thenReturn(Optional.of(testUser));

        UserDetails actualUser = userDetailsServiceImpl.loadUserByUsername(testUser.getUsername());

        assertThat(actualUser).isNotNull();
        assertThat(actualUser.getUsername()).isEqualTo(testUser.getUsername());
        assertThat(actualUser.getPassword()).isEqualTo(testUser.getPassword());
    }

    @Test
    void throws_username_not_found_exception_when_user_does_not_exist() {
        String nonExistentUsername = "nonexistentuser";
        when(userRepository.findByUsernameWithRoles(nonExistentUsername)).thenReturn(Optional.empty());

        assertThatExceptionOfType(UsernameNotFoundException.class)
                .isThrownBy(() -> userDetailsServiceImpl.loadUserByUsername(nonExistentUsername))
                .withMessageContaining(nonExistentUsername);
    }

}