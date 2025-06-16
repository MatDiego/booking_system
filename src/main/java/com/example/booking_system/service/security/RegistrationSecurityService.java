package com.example.booking_system.service.security;

import java.util.UUID;

public interface RegistrationSecurityService {
    boolean isOwner(UUID registrationId);
}
