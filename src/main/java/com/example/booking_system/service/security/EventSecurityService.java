package com.example.booking_system.service.security;

import java.util.UUID;

public interface EventSecurityService {
    boolean isEventOwner(UUID eventId);
}
