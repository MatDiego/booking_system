package com.example.booking_system.service.impl;

import com.example.booking_system.dto.request.RolesModificationRequestDto;
import com.example.booking_system.dto.response.UserResponseDto;
import com.example.booking_system.entity.Role;
import com.example.booking_system.entity.User;
import com.example.booking_system.entity.enums.RoleName;
import com.example.booking_system.mapper.UserMapper;
import com.example.booking_system.repository.RoleRepository;
import com.example.booking_system.repository.UserRepository;
import com.example.booking_system.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;
import java.util.UUID;

@Service
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
                .orElseThrow();
        //.orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));

        Set<Role> currentRoles = user.getRoles();

        requestDto.roleNames()
                .forEach(roleName -> {
                    Role role = roleRepository
                            .findByName(roleName)
                            .orElseThrow();
                    currentRoles.add(role);
                }
                );

        return userMapper.userToUserResponse(user);
    }

    @Override
    @Transactional
    public void removeRolesFromUser(UUID userId, RolesModificationRequestDto requestDto) {
        User user = userRepository.findById(userId)
                .orElseThrow();
        requestDto.roleNames()
                .forEach(roleName -> {
                    if(!roleName.equals(RoleName.ROLE_USER)){
                        roleRepository.findByName(roleName)
                                .ifPresent(role ->
                                        user.getRoles().remove(role));
                    }
                    // exception with user
                });
    }
}
