package com.example.booking_system.entity;

import com.example.booking_system.entity.enums.StatusType;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.SourceType;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "registrations", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"user_id", "event_id"}, name = "uq_user_event_registration")
})
public class Registration {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @NotNull
    private User user;
    @ManyToOne(fetch = FetchType.LAZY)
    @NotNull
    private Event event;
    @CreationTimestamp(source = SourceType.DB)
    @Column(updatable = false, nullable = false)
    private LocalDateTime registrationDate;
    @Enumerated(EnumType.STRING)
    @NotNull
    private StatusType status = StatusType.PENDING;
//    @Enumerated(EnumType.STRING)
//    private TicketType ticketType;


}
