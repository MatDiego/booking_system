package com.example.booking_system.service.impl;

import com.example.booking_system.dto.request.RolesModificationRequestDto;
import com.example.booking_system.dto.response.UserResponseDto;
import com.example.booking_system.entity.Role;
import com.example.booking_system.entity.User;
import com.example.booking_system.entity.enums.RoleName;
import com.example.booking_system.exception.ResourceNotFoundException;
import com.example.booking_system.mapper.UserMapper;
import com.example.booking_system.repository.RoleRepository;
import com.example.booking_system.repository.UserRepository;
import com.example.booking_system.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Validated
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final RoleRepository roleRepository;

    @Override
    @Transactional
    public UserResponseDto addRolesToUser(
            UUID userId,
            RolesModificationRequestDto requestDto
    ) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));

        Role userRole = roleRepository.findByName(RoleName.ROLE_USER)
                .orElseThrow(() -> new ResourceNotFoundException("Role", "name", RoleName.ROLE_USER.name()));

        Set<Role> newRoles = requestDto.roleNames().stream()
                .map(roleName -> roleRepository.findByName(roleName)
                        .orElseThrow(() -> new ResourceNotFoundException("Role", "name", roleName)))
                .collect(Collectors.toSet());

        newRoles.add(userRole);

        user.setRoles(newRoles);

        return userMapper.userToUserResponse(user);
    }
}
