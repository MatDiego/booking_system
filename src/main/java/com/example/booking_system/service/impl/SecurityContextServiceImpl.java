package com.example.booking_system.service.impl;

import com.example.booking_system.security.UserPrincipal;
import com.example.booking_system.service.SecurityContextService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class SecurityContextServiceImpl implements SecurityContextService {
    @Override
    public UUID getCurrentUserId() {
        return getCurrentUserPrincipal().getUserId();
    }

    @Override
    public UserPrincipal getCurrentUserPrincipal() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !(authentication.getPrincipal() instanceof UserPrincipal userPrincipal)) {
            throw new IllegalStateException("Cannot find authenticated user principal. Security context is empty or principal is of an unexpected type.");
        }
        return userPrincipal;
    }
}
