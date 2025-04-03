package com.example.booking_system.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.SourceType;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.annotations.UuidGenerator;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "users")
public class User {
    @Id
    @UuidGenerator(style = UuidGenerator.Style.RANDOM)
    private UUID id;
    @NotBlank
    @Column(unique = true)
    @Size(min = 4, max = 50)
    @Pattern(regexp = "^[a-zA-Z0-9_]+$")
    private String username;
    @Email
    @NotBlank
    @Column(unique = true)
    private String email;
    @NotBlank
    private String password;
    @NotBlank
    @Size(min = 2, max = 100)
    private String firstName;
    @NotBlank
    @Size(min = 2, max = 100)
    private String lastName;
    @OneToMany(
            mappedBy = Event_.ORGANIZER,
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private Set<Event> organizedEvents = new HashSet<>();
    @ManyToMany(
            cascade = {
                    CascadeType.PERSIST,
                    CascadeType.MERGE
            }
    )
    @JoinTable(name = "user_role",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    @NotNull
    private Set<Role> roles = new HashSet<>();
    @OneToMany(
            mappedBy = Registration_.USER,
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private Set<Registration> registrations = new HashSet<>();
    @NotNull
    @Column(name = "is_enabled")
    private Boolean enable = true;
    @CreationTimestamp(source = SourceType.VM)
    @Column(updatable = false)
    private LocalDateTime createdAt;
    @UpdateTimestamp(source = SourceType.VM)
    private LocalDateTime updatedAt;

    public void addRegistration(Registration registration) {
        registrations.add(registration);
        registration.setUser(this);
    }

    public void removeRegistration(Registration registration) {
        registrations.remove(registration);
        registration.setUser(null);
    }

    public void addOrganizedEvent(Event event) {
        organizedEvents.add(event);
        event.setOrganizer(this);
    }

    public void removeOrganizedEvent(Event event) {
        organizedEvents.remove(event);
        event.setOrganizer(null);
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User user)) return false;
        return Objects.equals(id, user.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
