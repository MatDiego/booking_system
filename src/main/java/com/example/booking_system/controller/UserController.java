package com.example.booking_system.controller;

import com.example.booking_system.dto.request.RolesModificationRequestDto;
import com.example.booking_system.dto.response.UserResponseDto;
import com.example.booking_system.service.impl.UserServiceImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserServiceImpl userService;

    @PutMapping("/{userId}/roles")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserResponseDto> addRoles(
            @PathVariable UUID userId,
            @Valid @RequestBody RolesModificationRequestDto requestDto
            ){
        UserResponseDto body = userService.addRolesToUser(userId, requestDto);
        return ResponseEntity.ok(body);
    }

}
