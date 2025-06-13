package com.example.booking_system.repository.specification;

import com.example.booking_system.entity.Event;
import com.example.booking_system.entity.Event_;
import com.example.booking_system.entity.User;
import com.example.booking_system.entity.User_;
import com.example.booking_system.entity.enums.EventType;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public class EventSpecifications {
    public static Specification<Event> hasEventType(EventType eventType) {
        return (root, query, criteriaBuilder) ->
                eventType == null ? null : criteriaBuilder.equal(root.get(Event_.eventType), eventType);
    }

    public static Specification<Event> isAfterDate(LocalDateTime dateFrom) {
        return (root, query, criteriaBuilder) ->
                dateFrom == null ? null : criteriaBuilder.greaterThanOrEqualTo(root.get(Event_.startDate), dateFrom);
    }

    public static Specification<Event> isBeforeDate(LocalDateTime dateTo) {
        return (root, query, criteriaBuilder) ->
                dateTo == null ? null : criteriaBuilder.lessThanOrEqualTo(root.get(Event_.endDate), dateTo);
    }

    public static Specification<Event> hasLocationLike(String location) {
        return (root, query, criteriaBuilder) ->
                location == null || location.isBlank() ? null : criteriaBuilder.like(criteriaBuilder.lower(root.get(Event_.location)), "%" + location.toLowerCase() + "%");
    }

    public static Specification<Event> hasPriceBetween(BigDecimal priceMin, BigDecimal priceMax) {
        return (root, query, criteriaBuilder) -> {
            Predicate minPredicate = priceMin == null ? null : criteriaBuilder.greaterThanOrEqualTo(root.get(Event_.price), priceMin);
            Predicate maxPredicate = priceMax == null ? null : criteriaBuilder.lessThanOrEqualTo(root.get(Event_.price), priceMax);

            if (minPredicate != null && maxPredicate != null) {
                return criteriaBuilder.and(minPredicate, maxPredicate);
            } else if (minPredicate != null) {
                return minPredicate;
            } else return maxPredicate;
        };
    }

    public static Specification<Event> hasOrganizer(UUID organizerId) {
        return (root, query, criteriaBuilder) -> {
            if (organizerId == null) {
                return null;
            }
            Join<Event, User> organizerJoin = root.join(Event_.organizer, JoinType.INNER);
            return criteriaBuilder.equal(organizerJoin.get(User_.id), organizerId);
        };
    }

    public static Specification<Event> titleContains(String title) {

        return (root, query, criteriaBuilder) ->
                title == null || title.isBlank() ? null : criteriaBuilder.like(criteriaBuilder.lower(root.get(Event_.title)), "%" + title.toLowerCase() + "%");
    }
}
