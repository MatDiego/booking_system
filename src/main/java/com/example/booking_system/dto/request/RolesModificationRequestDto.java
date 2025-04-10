package com.example.booking_system.dto.request;

import com.example.booking_system.entity.enums.RoleName;
import jakarta.validation.constraints.NotEmpty;

import java.util.Set;

public record RolesModificationRequestDto(
        @NotEmpty(message = "Role names list cannot be empty")
        Set<RoleName> roleNames
) {
}
