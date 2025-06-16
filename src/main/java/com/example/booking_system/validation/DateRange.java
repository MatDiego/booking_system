package com.example.booking_system.validation;

import java.time.LocalDateTime;

public interface DateRange {
    LocalDateTime getStartDate();
    LocalDateTime getEndDate();
}
