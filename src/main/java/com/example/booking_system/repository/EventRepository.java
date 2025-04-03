package com.example.booking_system.repository;

import com.example.booking_system.entity.Event;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface EventRepository extends JpaRepository<Event, UUID> {

    List<Event> findAllByStartDate(@NotNull @FutureOrPresent LocalDateTime startDate);
    Event saveEvent(@NotNull Event event);
}
