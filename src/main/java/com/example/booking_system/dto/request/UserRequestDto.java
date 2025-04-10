package com.example.booking_system.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;


@Builder
public record UserRequestDto(
        @NotBlank(message = "Username cannot be blank")
        @Size(min = 4, max = 50, message = "Username must be between 4 and 50 characters")
        @Pattern(regexp = "^[a-zA-Z0-9_]+$", message = "Username can only contain letters, numbers, and underscores")
        String username,

        @NotBlank(message = "Email cannot be blank")
        @Email(message = "Invalid email format")
        String email,

        @NotBlank(message = "Password cannot be blank")
        String password,

        @NotBlank(message = "First name cannot be blank")
        @Size(min = 2, max = 100, message = "First name must be between 2 and 100 characters")
        String firstName,

        @NotBlank(message = "Last name cannot be blank")
        @Size(min = 2, max = 100, message = "Last name must be between 2 and 100 characters")
        String lastName
) {}

