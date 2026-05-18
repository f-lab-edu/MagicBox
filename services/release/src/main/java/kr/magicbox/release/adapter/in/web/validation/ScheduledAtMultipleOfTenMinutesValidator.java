package kr.magicbox.release.adapter.in.web.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.time.Instant;
import java.time.ZoneOffset;

public class ScheduledAtMultipleOfTenMinutesValidator
        implements ConstraintValidator<ScheduledAtMultipleOfTenMinutes, Instant> {

    @Override
    public boolean isValid(Instant value, ConstraintValidatorContext context) {
        if (value == null) return true; // null 체크는 @NotNull이 담당
        int minute = value.atZone(ZoneOffset.UTC).getMinute();
        int second = value.atZone(ZoneOffset.UTC).getSecond();
        int nano = value.getNano();
        return minute % 10 == 0 && second == 0 && nano == 0;
    }
}
