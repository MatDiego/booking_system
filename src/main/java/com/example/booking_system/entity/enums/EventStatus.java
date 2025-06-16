package com.example.booking_system.entity.enums;

public enum EventStatus {
    SCHEDULED {
        @Override
        public boolean canTransitionTo(EventStatus newStatus) {
            return newStatus == COMPLETED || newStatus == CANCELED;
        }
    },
    CANCELED {
        @Override
        public boolean canTransitionTo(EventStatus newStatus) {
            return false;
        }
    },
    COMPLETED {
        @Override
        public boolean canTransitionTo(EventStatus newStatus) {
            return false;
        }
    };

    public abstract boolean canTransitionTo(EventStatus newStatus);
}
