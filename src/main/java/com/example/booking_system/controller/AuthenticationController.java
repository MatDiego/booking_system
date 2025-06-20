package com.example.booking_system.controller;

import com.example.booking_system.dto.request.AuthenticationRequestDto;
import com.example.booking_system.dto.response.AuthenticationResponseDto;
import com.example.booking_system.service.impl.AuthenticationServiceImpl;
import com.example.booking_system.dto.request.RegisterRequestDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationServiceImpl authenticationServiceImpl;
    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponseDto> register(
            @Valid @RequestBody RegisterRequestDto request
    ) {
        return ResponseEntity.ok(authenticationServiceImpl.register(request));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponseDto> authenticate(
            @Valid @RequestBody AuthenticationRequestDto request
    ) {
        return ResponseEntity.ok(authenticationServiceImpl.authenticate(request));
    }
}
