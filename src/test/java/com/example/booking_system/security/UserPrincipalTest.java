package com.example.booking_system.security;

import com.example.booking_system.entity.Role;
import com.example.booking_system.entity.User;
import com.example.booking_system.entity.enums.RoleName;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;
import java.util.Set;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class UserPrincipalTest {

    private User createUserWithRoles(Set<Role> roles) {
        User user = new User();
        user.setId(UUID.randomUUID());
        user.setUsername("testuser");
        user.setPassword("password");
        user.setEnable(true);
        user.setRoles(roles);
        return user;
    }

    private Role createRole(RoleName roleName) {
        Role role = new Role();
        role.setName(roleName);
        return role;
    }

    private Role roleUser;
    private Role roleAdmin;
    private Role roleOrganizer;


    @BeforeEach
    void setUp() {
        roleUser = createRole(RoleName.ROLE_USER);
        roleAdmin = createRole(RoleName.ROLE_ADMIN);
        roleOrganizer = createRole(RoleName.ROLE_ORGANIZER);
    }

    @Test
    void maps_user_roles_to_granted_authorities() {
        User user = createUserWithRoles(Set.of(roleUser, roleAdmin, roleOrganizer));
        UserPrincipal userPrincipal = new UserPrincipal(user);

        Collection<? extends GrantedAuthority> authorities = userPrincipal.getAuthorities();

        assertThat(authorities)
                .isNotNull()
                .hasSize(3)
                .extracting(GrantedAuthority::getAuthority)
                .containsExactlyInAnyOrder("ROLE_USER", "ROLE_ADMIN", "ROLE_ORGANIZER");
    }

    @Test
    void returns_empty_authorities_when_user_roles_set_is_null() {
        User user = createUserWithRoles(null);
        UserPrincipal userPrincipal = new UserPrincipal(user);

        assertThat(userPrincipal.getAuthorities()).isNotNull().isEmpty();
    }

    @Test
    void returns_empty_authorities_when_user_roles_set_is_empty() {
        User user = createUserWithRoles(Set.of());
        UserPrincipal userPrincipal = new UserPrincipal(user);

        assertThat(userPrincipal.getAuthorities()).isNotNull().isEmpty();
    }

    @Test
    void exposes_user_id_from_underlying_user() {
        User user = createUserWithRoles(Set.of(roleUser));
        UserPrincipal userPrincipal = new UserPrincipal(user);
        UUID expectedId = user.getId();

        UUID userId = userPrincipal.getUserId();

        assertThat(userId).isNotNull().isEqualTo(expectedId);
    }

    @Test
    void exposes_password_from_underlying_user() {
        User user = createUserWithRoles(Set.of(roleUser));
        UserPrincipal userPrincipal = new UserPrincipal(user);
        String expectedPassword = user.getPassword();

        String actualPassword = userPrincipal.getPassword();

        assertThat(actualPassword).isNotNull().isEqualTo(expectedPassword);
    }

    @Test
    void exposes_username_from_underlying_user() {
        User user = createUserWithRoles(Set.of(roleUser));
        UserPrincipal userPrincipal = new UserPrincipal(user);
        String expectedUsername = user.getUsername();

        String actualUsername = userPrincipal.getUsername();

        assertThat(actualUsername).isNotNull().isEqualTo(expectedUsername);
    }

    @Test
    void isEnableShouldReturnTrueWhenUserIsEnabled() {
        User user = createUserWithRoles(Set.of(roleUser));
        UserPrincipal userPrincipal = new UserPrincipal(user);

        assertThat(userPrincipal.isEnabled()).isTrue();
    }

    @Test
    void reports_enabled_as_false_when_underlying_user_is_disabled() {
        User user = createUserWithRoles(Set.of(roleUser));
        user.setEnable(false);
        UserPrincipal userPrincipal = new UserPrincipal(user);

        assertThat(userPrincipal.isEnabled()).isFalse();
    }
}