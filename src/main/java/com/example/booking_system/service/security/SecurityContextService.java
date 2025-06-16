package com.example.booking_system.service.security;

import com.example.booking_system.security.UserPrincipal;

import java.util.UUID;

public interface SecurityContextService {
    UUID getCurrentUserId();
    UserPrincipal getCurrentUserPrincipal();
}
