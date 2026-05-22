package kr.magicbox.release.adapter.in.web.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.time.Instant;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;

public class ScheduledAtMultipleOfTenMinutesValidator
        implements ConstraintValidator<ScheduledAtMultipleOfTenMinutes, Instant> {

    private static final int MIN_MINUTES_FROM_NOW = 10;

    @Override
    public boolean isValid(Instant value, ConstraintValidatorContext context) {
        if (value == null) return true; // null 체크는 @NotNull이 담당
        ZonedDateTime zonedDateTime = value.atZone(ZoneOffset.UTC); // KST(+09:00)는 분 오프셋 없음 — UTC 기준과 동일
        boolean isMultipleOfTen = zonedDateTime.getMinute() % 10 == 0
                && zonedDateTime.getSecond() == 0
                && zonedDateTime.getNano() == 0;
        boolean isFarEnough = value.isAfter(Instant.now().plusSeconds(MIN_MINUTES_FROM_NOW * 60L));
        return isMultipleOfTen && isFarEnough;
    }
}
