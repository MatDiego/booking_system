package com.example.booking_system.validation;

import com.example.booking_system.entity.Event;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.time.LocalDateTime;

public class EndDateAfterStartDateValidator implements ConstraintValidator<EndDateAfterStartDate, Event> {

    @Override
    public boolean isValid(Event event, ConstraintValidatorContext context) {
        LocalDateTime startDate = event.getStartDate();
        LocalDateTime endDate = event.getEndDate();

        if (startDate == null || endDate == null) {
            return true;
        }

        boolean isValid = endDate.isAfter(startDate);

        if (!isValid) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(context.getDefaultConstraintMessageTemplate())
                    .addPropertyNode("endDate")
                    .addConstraintViolation();
        }

        return isValid;
    }
}
