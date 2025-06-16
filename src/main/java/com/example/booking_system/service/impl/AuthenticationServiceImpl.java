package com.example.booking_system.service.impl;

import com.example.booking_system.dto.request.AuthenticationRequestDto;
import com.example.booking_system.dto.request.RegisterRequestDto;
import com.example.booking_system.dto.response.AuthenticationResponseDto;
import com.example.booking_system.entity.Role;
import com.example.booking_system.entity.User;
import com.example.booking_system.entity.enums.RoleName;
import com.example.booking_system.mapper.AuthMapper;
import com.example.booking_system.repository.RoleRepository;
import com.example.booking_system.repository.UserRepository;
import com.example.booking_system.security.UserPrincipal;
import com.example.booking_system.service.AuthenticationService;
import com.example.booking_system.security.jwt.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.util.Set;

@Service
@Validated
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthMapper authMapper;
    private final RoleRepository roleRepository;
    private final AuthenticationManager authenticationManager;

    @Override
    @Transactional
    public AuthenticationResponseDto register(RegisterRequestDto request) {
        User user = authMapper.registerRequestDtoToUser(request);
        user.setPassword(passwordEncoder.encode(request.password()));
        Role userRole = roleRepository.findByName(RoleName.ROLE_USER)
                .orElseThrow(() -> new RuntimeException("Error: Role ROLE_USER is not found."));
        user.setRoles(Set.of(userRole));
        User savedUser = userRepository.save(user);
        UserPrincipal userPrincipal = new UserPrincipal(savedUser);
        String jwtToken = jwtService.generateToken(userPrincipal);

        return AuthenticationResponseDto.builder()
                .token(jwtToken)
                .build();
    }

    @Override
    @Transactional(readOnly = true)
    public AuthenticationResponseDto authenticate(AuthenticationRequestDto request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.username(),
                        request.password()
                )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();

        String jwtToken = jwtService.generateToken(userPrincipal);
        return AuthenticationResponseDto.builder()
                .token(jwtToken)
                .build();
    }
}
