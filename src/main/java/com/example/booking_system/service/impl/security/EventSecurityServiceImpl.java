package com.example.booking_system.service.impl.security;

import com.example.booking_system.repository.EventRepository;
import com.example.booking_system.security.UserPrincipal;
import com.example.booking_system.service.security.EventSecurityService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service("eventSecurityService")
@RequiredArgsConstructor
public class EventSecurityServiceImpl implements EventSecurityService {

    private final EventRepository eventRepository;

    @Override
    public boolean isEventOwner(UUID eventId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !(authentication.getPrincipal() instanceof UserPrincipal currentUser)) {
            return false;
        }
        UUID currentUserId = currentUser.getUserId();
        return eventRepository.findById(eventId)
                .map(event -> event.getOrganizer().getId().equals(currentUserId))
                .orElse(false);
    }

}
