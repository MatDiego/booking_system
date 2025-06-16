package com.example.booking_system.validation;

import com.example.booking_system.entity.Event;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.time.LocalDateTime;

public class EndDateAfterStartDateValidator implements ConstraintValidator<EndDateAfterStartDate, DateRange> {

    @Override
    public boolean isValid(DateRange dateRangeObject, ConstraintValidatorContext context) {
        LocalDateTime startDate = dateRangeObject.getStartDate();
        LocalDateTime endDate = dateRangeObject.getEndDate();

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
