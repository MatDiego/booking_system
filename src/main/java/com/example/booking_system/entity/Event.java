package com.example.booking_system.entity;

import com.example.booking_system.entity.enums.EventType;
import com.example.booking_system.validation.EndDateAfterStartDate;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.SourceType;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.annotations.UuidGenerator;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@Entity
@EndDateAfterStartDate
@Table(name = "events")
public class Event {
    @Id
    @UuidGenerator(style = UuidGenerator.Style.RANDOM)
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    @NotBlank
    @Size(min = 2, max = 255)
    private String title;
    @NotBlank
    @Size(min = 10)
    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "organizer_id")
    @NotNull
    private User organizer;
    @NotNull
    @FutureOrPresent
    private LocalDateTime startDate;
    @NotNull
    @Future
    private LocalDateTime endDate;
    @NotBlank
    @Size(max = 50)
    private String location;
    @NotNull
    @Positive
    private Integer capacity;

    @Enumerated(EnumType.STRING)
    @NotNull
    private EventType eventType;
    @NotNull
    @PositiveOrZero
    @Digits(
            integer = 5,
            fraction = 2
    )
    private BigDecimal price;
//    private Set<String> tags;
    @CreationTimestamp(source = SourceType.DB)
    @Column(updatable = false)
    private LocalDateTime createdAt;
    @UpdateTimestamp(source = SourceType.DB)
    private LocalDateTime updatedAt;
    @OneToMany(
            mappedBy = Registration_.EVENT,
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private Set<Registration> registrations = new HashSet<>();

    public void addRegistration(Registration registration) {
        registrations.add(registration);
        registration.setEvent(this);
    }

    public void removeRegistration(Registration registration) {
        registrations.remove(registration);
        registration.setEvent(null);
    }


}
