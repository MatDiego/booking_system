package com.example.booking_system.repository;

import com.example.booking_system.entity.Registration;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface RegistrationRepository extends JpaRepository<Registration, UUID> {
    boolean existsByUserIdAndEventId(UUID userId, UUID eventId);

    Page<Registration> findByUserId(UUID userId, Pageable pageable);

    @Query("SELECT r FROM Registration r JOIN FETCH r.user WHERE r.event.id = :eventId")
    Page<Registration> findByEventIdWithUser(@Param("eventId") UUID eventId, Pageable pageable);

}
