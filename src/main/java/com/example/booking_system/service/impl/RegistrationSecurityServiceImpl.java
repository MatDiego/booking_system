package com.example.booking_system.service.impl;

import com.example.booking_system.repository.RegistrationRepository;
import com.example.booking_system.security.UserPrincipal;
import com.example.booking_system.service.RegistrationSecurityService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service("registrationSecurityService")
@RequiredArgsConstructor
public class RegistrationSecurityServiceImpl implements RegistrationSecurityService {

    private final RegistrationRepository registrationRepository;

    @Override
    public boolean isOwner(UUID registrationId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !(authentication.getPrincipal() instanceof UserPrincipal currentUser)) {
            return false;
        }
        UUID currentUserId = currentUser.getUserId();
        return registrationRepository.findById(registrationId)
                .map(registration -> registration.getUser().getId().equals(currentUserId))
                .orElse(false);
    }
}
