package com.example.booking_system.entity.enums;

public enum RegistrationStatus {
    PENDING {
        @Override
        public boolean canTransitionTo(RegistrationStatus newStatus) {
            return newStatus == CONFIRMED || newStatus == REJECTED || newStatus == CANCELED_BY_USER;
        }
    },
    CONFIRMED {
        @Override
        public boolean canTransitionTo(RegistrationStatus newStatus) {
            return newStatus == CANCELED_BY_USER || newStatus == CANCELED_BY_EVENT;
        }
    },
    CANCELED_BY_EVENT {
        @Override
        public boolean canTransitionTo(RegistrationStatus newStatus) {
            return false;
        }
    },
    CANCELED_BY_USER {
        @Override
        public boolean canTransitionTo(RegistrationStatus newStatus) {
            return false;
        }
    },
    REJECTED {
        @Override
        public boolean canTransitionTo(RegistrationStatus newStatus) {
            return false;
        }
    };

    public abstract boolean canTransitionTo(RegistrationStatus newStatus);
}
